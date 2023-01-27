package cloud.bangover.functions.registry;

import cloud.bangover.actors.Actor;
import cloud.bangover.actors.Message;
import cloud.bangover.async.promises.Deferred;
import cloud.bangover.errors.UnexpectedErrorException;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.timer.Timeout;
import cloud.bangover.timer.TimeoutSupervisor;
import cloud.bangover.timer.Timer;
import lombok.NonNull;

public class BusinessFunctionExecutionActor extends Actor<Object> {
  private final BusinessFunctionExecutor functionExecutor;
  private final TimeoutSupervisor timeoutSupervisor;

  private BusinessFunctionExecutionActor(@NonNull Context context, @NonNull Class<?> requestType,
      @NonNull Class<?> responseType, @NonNull BusinessFunction<Object, Object> function,
      Timeout timeout) {
    super(context);
    this.functionExecutor = new BusinessFunctionExecutor(requestType, responseType, function);
    this.timeoutSupervisor = Timer.supervisor(timeout, () -> {
      restart(self());
    });
  }

  @SuppressWarnings("unchecked")
  public static <Q, S> Factory<Object> factory(Class<?> requestType, Class<?> responseType,
      BusinessFunction<Q, S> businessFunction, Timeout timeout) {
    return context -> new BusinessFunctionExecutionActor(context, requestType, responseType,
        (BusinessFunction<Object, Object>) businessFunction, timeout);
  }

  @Override
  protected void receive(Message<Object> message) {
    functionExecutor.executeFunction(message.getBody(), new Deferred<Object>() {
      @Override
      public void resolve(Object response) {
        tell(message.replyWith(response));
      }

      @Override
      public void reject(Exception error) {
        timeoutSupervisor.stopSupervision();
        tell(message.replyWith(error));
      }
    }, timeoutSupervisor);
  }

  @Override
  protected FaultResolver<Object> getFaultResover() {
    return ((lifecycleController, message, throwable) -> {
      tell(message.replyWith(new UnexpectedErrorException(throwable)));
      lifecycleController.resume();
    });
  }
}
