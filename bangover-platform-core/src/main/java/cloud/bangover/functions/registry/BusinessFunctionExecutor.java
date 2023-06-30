package cloud.bangover.functions.registry;

import cloud.bangover.BoundedContextId;
import cloud.bangover.async.promises.Deferred;
import cloud.bangover.errors.ApplicationException;
import cloud.bangover.errors.UnexpectedErrorException;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.IncompatibleTypeException;
import cloud.bangover.timer.TimeoutSupervisor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class BusinessFunctionExecutor {
  private final Class<?> requestType;
  private final Class<?> responseType;
  private final BoundedContextId boundedContextId;
  private final BusinessFunction<Object, Object> function;

  public void executeFunction(Object request, Deferred<Object> deferred,
      TimeoutSupervisor timeoutSupervisor) {
    executeSafe(deferred, () -> {
      checkRequestType(request);
      timeoutSupervisor.startSupervision();
      function.invoke(new BusinessFunction.Context<Object, Object>() {
        @Override
        public BoundedContextId getBoundedContextId() {
          return boundedContextId;
        }

        @Override
        public Object getRequest() {
          return request;
        }

        @Override
        public void reply(Object response) {
          executeSafe(deferred, () -> {
            timeoutSupervisor.stopSupervision();
            checkResponseType(response);
            deferred.resolve(response);
          });
        }

        @Override
        public void reject(Exception error) {
          executeSafe(deferred, () -> {
            timeoutSupervisor.stopSupervision();
            deferred.reject(error);
          });
        }
      });
    });
  }

  private void checkRequestType(Object request) {
    if (!requestHasExpectedType(request) && !nullRequestIsExpectedOnVoidType(request)) {
      throw new IncompatibleTypeException(requestType, request);
    }
  }

  private boolean requestHasExpectedType(Object request) {
    return requestType.isInstance(request);
  }

  private boolean nullRequestIsExpectedOnVoidType(Object request) {
    return requestType == Void.class && request == null;
  }

  private void checkResponseType(Object response) {
    if (!responseType.isInstance(response) && response != null) {
      throw new IncompatibleTypeException(responseType, response);
    }
  }

  private void executeSafe(Deferred<Object> deferred, Runnable action) {
    try {
      action.run();
    } catch (ApplicationException error) {
      deferred.reject(error);
    } catch (Throwable error) {
      deferred.reject(new UnexpectedErrorException(error));
    }
  }
}
