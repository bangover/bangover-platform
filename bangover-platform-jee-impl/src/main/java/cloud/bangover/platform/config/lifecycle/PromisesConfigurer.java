package cloud.bangover.platform.config.lifecycle;

import cloud.bangover.actors.ActorSystem;
import cloud.bangover.async.promises.ActorSystemBasedDeferredFunctionRunner;
import cloud.bangover.async.promises.Promises;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PromisesConfigurer {
  @Inject
  private ActorSystem actorSystem;

  public void configureAsyncExecution() {
    Promises.configureDeferredFunctionRunner(
        ActorSystemBasedDeferredFunctionRunner.createFor(actorSystem));
  }
}
