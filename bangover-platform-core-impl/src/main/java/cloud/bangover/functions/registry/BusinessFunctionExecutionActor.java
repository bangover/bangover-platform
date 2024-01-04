package cloud.bangover.functions.registry;

import cloud.bangover.actors.Actor;
import cloud.bangover.actors.Message;
import cloud.bangover.async.Async;
import cloud.bangover.async.promises.Deferred;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.timer.Timeout;
import cloud.bangover.timer.TimeoutException;
import cloud.bangover.timer.TimeoutSupervisor;
import cloud.bangover.timer.Timer;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class BusinessFunctionExecutionActor extends Actor<InvokeBusinessFunction<Object, Object>> {
  static final String ASYNC_CONTEXT_ID = "Async.ContextId";

  private final BusinessFunctionExecutor functionExecutor;
  private final Timeout timeout;

  private BusinessFunctionExecutionActor(@NonNull Context context, @NonNull Class<?> requestType,
      @NonNull Class<?> responseType, @NonNull BusinessFunction<Object, Object> function,
      Timeout timeout) {
    super(context);
    this.functionExecutor = new BusinessFunctionExecutor(requestType, responseType, function);
    this.timeout = timeout;
  }

  @SuppressWarnings("unchecked")
  public static <Q, S> Factory<InvokeBusinessFunction<Object, Object>> factory(Class<?> requestType,
      Class<?> responseType, BusinessFunction<Q, S> businessFunction, Timeout timeout) {
    return context -> new BusinessFunctionExecutionActor(context, requestType, responseType,
        (BusinessFunction<Object, Object>) businessFunction, timeout);
  }

  @Override
  protected void receive(Message<InvokeBusinessFunction<Object, Object>> message) {
    executeInsideMessageContext(message, () -> {
      InvokeBusinessFunction<Object, Object> command = message.getBody();
      FunctionInvocationDeferred invocationDeferred =
          new FunctionInvocationDeferred(command.getDeferred());
      TimeoutSupervisor timeoutSupervisor = Timer.supervisor(timeout, () -> {
        invocationDeferred.markAsTimedOut();
        command.getDeferred().reject(new TimeoutException(timeout));
        restart(self());
      });
      functionExecutor.executeFunction(command.getRequest(), invocationDeferred, timeoutSupervisor);
    });
  }

  @SuppressWarnings("unchecked")
  private void executeInsideMessageContext(Message<InvokeBusinessFunction<Object, Object>> message,
      Runnable callback) {
    Optional<String> metadataAttribute =
        message.getMetadataAttribute(Optional.class, ASYNC_CONTEXT_ID)
            .flatMap(value -> value.map(String.class::cast));
    Async.executeInsideContext(metadataAttribute, callback);
  }

  @RequiredArgsConstructor
  private static class FunctionInvocationDeferred implements Deferred<Object> {
    private final Deferred<Object> wrapped;
    private boolean timedOut = false;

    @Override
    public void resolve(Object response) {
      runIfNotTimedOut(() -> wrapped.resolve(response));
    }

    @Override
    public void reject(Exception error) {
      runIfNotTimedOut(() -> wrapped.reject(error));
    }

    public void markAsTimedOut() {
      this.timedOut = true;
    }

    private void runIfNotTimedOut(Runnable runnable) {
      if (!timedOut) {
        runnable.run();
      }
    }
  }
}
