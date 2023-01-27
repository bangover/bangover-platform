package cloud.bangover.async.promises;

import cloud.bangover.actors.ActorAddress;
import cloud.bangover.actors.ActorName;
import cloud.bangover.actors.ActorSystem;
import cloud.bangover.actors.Message;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ActorSystemBasedDeferredFunctionRunner implements Promises.DeferredFunctionRunner {
  private final ActorSystem actorSystem;
  private final ActorAddress actorAddress;

  public static Promises.DeferredFunctionRunner createFor(ActorSystem actorSystem) {
    ActorName actorName =
        ActorName.wrap(String.format("DEFERRED-FUNC-RUNNER--%s", UUID.randomUUID()));
    ActorAddress actorAddress =
        actorSystem.actorOf(actorName, DeferredFunctionRunnerActor.factory());
    return new ActorSystemBasedDeferredFunctionRunner(actorSystem, actorAddress);
  }

  @Override
  public <T> void executeDeferredOperation(Deferred.DeferredFunction<T> deferredFunction,
      Deferred<T> deferred) {
    RunDeferredFunction<T> command = new RunDeferredFunction<>(deferredFunction, deferred);
    Message<RunDeferredFunction<T>> message = Message.createFor(actorAddress, command);
    actorSystem.tell(message);
  }
}
