package cloud.bangover.functions;

import cloud.bangover.async.promises.Promise;
import cloud.bangover.async.promises.Promises;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "createFor")
public class MockFunctionRunner<Q, S> {
  private final BusinessFunction<Q, S> function;

  public Promise<S> executeFunction() {
    return executeFunction(null);
  }
  
  public Promise<S> executeFunction(Q request) {
    return Promises.of(deferred -> {
      function.invoke(new BusinessFunction.Context<Q, S>() {
        @Override
        public Q getRequest() {
          return request;
        }

        @Override
        public void reply(S response) {
          deferred.resolve(response);
        }

        @Override
        public void reject(Throwable error) {
          deferred.reject(error);
        }
      });
    });
  }
}
