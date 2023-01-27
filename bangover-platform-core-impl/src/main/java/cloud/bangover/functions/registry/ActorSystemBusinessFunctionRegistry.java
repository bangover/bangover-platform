package cloud.bangover.functions.registry;

import cloud.bangover.actors.Actor;
import cloud.bangover.actors.ActorAddress;
import cloud.bangover.actors.ActorName;
import cloud.bangover.actors.ActorSystem;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.generators.Generator;
import cloud.bangover.interactions.interactor.ActorSystemInteractor;
import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.interactions.interactor.TargetAddress;
import cloud.bangover.timer.Timeout;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ActorSystemBusinessFunctionRegistry implements BusinessFunctionRegistry {
  private final ActorSystem actorSystem;

  private final Generator<ActorName> actorNameGenerator =
      () -> ActorName.wrap(String.format("FUNCTION--%s", UUID.randomUUID()));

  @Override
  public <S> ReplyOnlyInteractor<S> registerReplyOnlyFunction(Class<?> responseType,
      BusinessFunction<Void, S> businessFunction, Timeout timeout) {
    TargetAddress targetAddress =
        registerBusinessFunctionActor(Void.class, responseType, businessFunction, timeout);
    ReplyOnlyInteractor.Factory interactorFactory =
        ActorSystemInteractor.replyOnlyFactory(actorSystem);
    return () -> {
      @SuppressWarnings("unchecked")
      ReplyOnlyInteractor<S> interactor =
          interactorFactory.createInteractor(targetAddress, (Class<S>) responseType, timeout);
      return interactor.invoke();
    };
  }

  @Override
  public <Q, S> RequestReplyInteractor<Q, S> registerRequestReplyFunction(Class<?> requestType,
      Class<?> responseType, BusinessFunction<Q, S> businessFunction, Timeout timeout) {
    TargetAddress targetAddress =
        registerBusinessFunctionActor(requestType, responseType, businessFunction, timeout);
    RequestReplyInteractor.Factory interactorFactory =
        ActorSystemInteractor.requestReplyFactory(actorSystem);
    return request -> {
      @SuppressWarnings("unchecked")
      RequestReplyInteractor<Q, S> interactor = interactorFactory.createInteractor(targetAddress,
          (Class<Q>) requestType, (Class<S>) responseType, timeout);
      return interactor.invoke(request);
    };
  }

  private <Q, S> TargetAddress registerBusinessFunctionActor(Class<?> requestType,
      Class<?> responseType, BusinessFunction<Q, S> businessFunction, Timeout timeout) {
    Actor.Factory<Object> actorFactory = BusinessFunctionExecutionActor.factory(requestType,
        responseType, businessFunction, timeout);
    ActorAddress actorAddress =
        actorSystem.actorOf(actorNameGenerator.generateNext(), actorFactory);
    return TargetAddress.ofUrn(actorAddress.toString());
  }
}
