package cloud.bangover.functions;

import cloud.bangover.async.promises.Promise;
import cloud.bangover.async.promises.Promises;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class BusinessFunctionsCascade<Q, S, R> implements BusinessFunction<Q, R> {
  private final BusinessFunction<Q, S> original;
  private final BusinessFunction<S, R> chained;

  private static <Q, S> Promise<S> executeFunction(Q request, BusinessFunction<Q, S> function) {
    return Promises.of(deferred -> {
      function.invoke(new Context<Q, S>() {
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

  @Override
  public void invoke(Context<Q, R> context) {
    executeFunction(context.getRequest(), original).chain(response -> {
      return executeFunction(response, chained);
    }).then(context::reply).error(context::reject);
  }
}
