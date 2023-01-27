package cloud.bangover.functions.registry;

import cloud.bangover.async.promises.Deferred;
import cloud.bangover.async.promises.Promises;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.timer.Timeout;
import cloud.bangover.timer.TimeoutException;
import cloud.bangover.timer.TimeoutSupervisor;
import cloud.bangover.timer.Timer;

public class DefaultBusinessFunctionExecutor implements BusinessFunctionRegistry {
  @Override
  public <S> ReplyOnlyInteractor<S> registerReplyOnlyFunction(Class<?> responseType,
      BusinessFunction<Void, S> businessFunction, Timeout timeout) {
    RequestReplyInteractor<Void, S> interactor =
        registerRequestReplyFunction(Void.class, responseType, businessFunction, timeout);
    return () -> interactor.invoke(null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Q, S> RequestReplyInteractor<Q, S> registerRequestReplyFunction(Class<?> requestType,
      Class<?> responseType, BusinessFunction<Q, S> businessFunction, Timeout timeout) {
    BusinessFunctionExecutor functionExecutor = new BusinessFunctionExecutor(requestType,
        responseType, (BusinessFunction<Object, Object>) businessFunction);
    return request -> Promises.of(deferred -> {
      TimeoutSupervisor timeoutSupervisor =
          Timer.supervisor(timeout, () -> deferred.reject(new TimeoutException(timeout)));
      functionExecutor.executeFunction(request, (Deferred<Object>) deferred, timeoutSupervisor);
    });
  }
}
