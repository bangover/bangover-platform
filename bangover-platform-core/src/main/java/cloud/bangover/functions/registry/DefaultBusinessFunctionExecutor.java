package cloud.bangover.functions.registry;

import cloud.bangover.BoundedContextId;
import cloud.bangover.async.promises.Deferred;
import cloud.bangover.async.promises.Promises;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.timer.Timeout;
import cloud.bangover.timer.TimeoutException;
import cloud.bangover.timer.TimeoutSupervisor;
import cloud.bangover.timer.Timer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultBusinessFunctionExecutor implements BusinessFunctionRegistry {
  private final BoundedContextId boundedContextId;

  public DefaultBusinessFunctionExecutor() {
    this(BoundedContextId.PLATFORM_CONTEXT);
  }

  @Override
  public <S> FunctionReplyOnlyInteractor<S> registerReplyOnlyFunction(Class<?> responseType,
      BusinessFunction<Void, S> businessFunction, Timeout timeout) {
    RequestReplyInteractor<Void, S> interactor =
        registerRequestReplyFunction(Void.class, responseType, businessFunction, timeout);
    return new FunctionReplyOnlyInteractorWrapper<>(boundedContextId,
        () -> interactor.invoke(null));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Q, S> FunctionRequestReplyInteractor<Q, S> registerRequestReplyFunction(
      Class<?> requestType, Class<?> responseType, BusinessFunction<Q, S> businessFunction,
      Timeout timeout) {
    BusinessFunctionExecutor functionExecutor = new BusinessFunctionExecutor(requestType,
        responseType, boundedContextId, (BusinessFunction<Object, Object>) businessFunction);
    return new FunctionRequestReplyInteractorWrapper<>(boundedContextId,
        request -> Promises.of(deferred -> {
          TimeoutSupervisor timeoutSupervisor =
              Timer.supervisor(timeout, () -> deferred.reject(new TimeoutException(timeout)));
          functionExecutor.executeFunction(request, (Deferred<Object>) deferred, timeoutSupervisor);
        }));
  }
}
