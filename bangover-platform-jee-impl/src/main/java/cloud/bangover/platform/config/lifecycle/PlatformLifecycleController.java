package cloud.bangover.platform.config.lifecycle;

import cloud.bangover.actors.ActorSystem;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class PlatformLifecycleController {
  @Inject
  private ActorSystem actorSystem;

  @Inject
  private TextProcessingConfigurer textProcessingConfigurer;

  @Inject
  private GlobalValidationConfigurer globalValidationConfigurer;

  @Inject
  private PromisesConfigurer promisesConfigurer;

  @Inject
  private GlobalEventsConfigurer globalEventsConfigurer;

  public void onStart(@Observes @Initialized(ApplicationScoped.class) Object pointless) {
    textProcessingConfigurer.configureTextProcessing();
    globalValidationConfigurer.registerGlobalValidations();
    promisesConfigurer.configureAsyncExecution();
    globalEventsConfigurer.configureEventBuses();
    actorSystem.start();
  }

  public void onStop(@Observes @Destroyed(ApplicationScoped.class) Object pointless) {
    actorSystem.shutdown();
    globalEventsConfigurer.destroyEventBuses();
    // TODO Create promises reset logic
    globalValidationConfigurer.destroyGlobalValidations();
    textProcessingConfigurer.destroyTextProcessing();
  }
}
