package cloud.bangover.functions.registry;

import cloud.bangover.BoundedContextId;
import cloud.bangover.actors.Actor;
import cloud.bangover.actors.ActorAddress;
import cloud.bangover.actors.ActorName;
import cloud.bangover.actors.ActorSystem;
import cloud.bangover.actors.Message;
import cloud.bangover.async.Async;
import cloud.bangover.async.AsyncContext;
import cloud.bangover.async.promises.Promise;
import cloud.bangover.async.promises.Promises;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.generators.Generator;
import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.timer.Timeout;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ActorSystemBusinessFunctionRegistry implements BusinessFunctionRegistry {
  private final BoundedContextId boundedContextId;
  private final ActorSystem actorSystem;

  private final Generator<ActorName> actorNameGenerator =
      () -> ActorName.wrap(String.format("FUNCTION--%s", UUID.randomUUID()));

  @Override
  public <S> FunctionReplyOnlyInteractor<S> registerReplyOnlyFunction(Class<?> responseType,
      BusinessFunction<Void, S> businessFunction, Timeout timeout) {
    ReplyOnlyInteractor<S> interactor =
        new ReplyOnlyFunctionInteractor<S>(responseType, businessFunction, timeout);
    return new FunctionReplyOnlyInteractorWrapper<>(boundedContextId, interactor);
  }

  @Override
  public <Q, S> FunctionRequestReplyInteractor<Q, S> registerRequestReplyFunction(
      Class<?> requestType, Class<?> responseType, BusinessFunction<Q, S> businessFunction,
      Timeout timeout) {
    RequestReplyInteractor<Q, S> interactor = new RequestReplyFunctionInteractor<Q, S>(requestType,
        responseType, businessFunction, timeout);
    return new FunctionRequestReplyInteractorWrapper<>(boundedContextId, interactor);
  }

  private class RequestReplyFunctionInteractor<Q, S> implements RequestReplyInteractor<Q, S> {
    private final ActorAddress actorAddress;
    private final AsyncContext.LifecycleController controller = Async.getController();

    public RequestReplyFunctionInteractor(Class<?> requestType, Class<?> responseType,
        BusinessFunction<Q, S> businessFunction, Timeout timeout) {
      super();
      Actor.Factory<InvokeBusinessFunction<Object, Object>> actorFactory =
          BusinessFunctionExecutionActor.factory(requestType, responseType, boundedContextId,
              businessFunction, timeout);
      this.actorAddress = actorSystem.actorOf(actorNameGenerator.generateNext(), actorFactory);
    }

    @Override
    public Promise<S> invoke(Q request) {
      return Promises.of(deferred -> {
        Message<InvokeBusinessFunction<Q, S>> message =
            Message.createFor(actorAddress, new InvokeBusinessFunction<Q, S>(request, deferred))
                .withMetadata(BusinessFunctionExecutionActor.ASYNC_CONTEXT_ID,
                    controller.getCurrentContextId());
        actorSystem.tell(message);
      });
    }
  }

  private class ReplyOnlyFunctionInteractor<S> extends RequestReplyFunctionInteractor<Void, S>
      implements ReplyOnlyInteractor<S> {
    public ReplyOnlyFunctionInteractor(Class<?> responseType,
        BusinessFunction<Void, S> businessFunction, Timeout timeout) {
      super(Void.class, responseType, businessFunction, timeout);
    }

    @Override
    public Promise<S> invoke() {
      return super.invoke(null);
    }
  }
}
