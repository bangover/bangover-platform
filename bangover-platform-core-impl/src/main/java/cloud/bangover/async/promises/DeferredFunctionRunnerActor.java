package cloud.bangover.async.promises;

import cloud.bangover.actors.Actor;
import cloud.bangover.actors.Message;
import lombok.NonNull;

public class DeferredFunctionRunnerActor extends Actor<RunDeferredFunction<Object>> {
  private DeferredFunctionRunnerActor(@NonNull Context context) {
    super(context);
  }

  public static Factory<RunDeferredFunction<Object>> factory() {
    return DeferredFunctionRunnerActor::new;
  }

  @Override
  protected void receive(Message<RunDeferredFunction<Object>> message) throws Exception {
    RunDeferredFunction<Object> command = message.getBody();
    command.getDeferredFunction().execute(command.getDeferred());
  }
}
