package cloud.bangover.platform.config;

import cloud.bangover.actors.ActorSystem;
import cloud.bangover.actors.Actors;
import cloud.bangover.actors.EventLoop;
import cloud.bangover.actors.ExecutorServiceDispatcher;
import cloud.bangover.platform.PlatformSettings;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class ActorSystemConfiguration {
  @Inject
  private PlatformSettings platformSettings;

  @Produces
  @ApplicationScoped
  public EventLoop.Dispatcher eventLoopDispatcher() {
    return ExecutorServiceDispatcher.createFor(platformSettings.executorService());
  }

  @Produces
  @ApplicationScoped
  public ActorSystem actorSystem(EventLoop.Dispatcher eventLoopDispatcher) {
    return Actors.create(systemConfigurer -> systemConfigurer
        .withCorrelationKeyGenerator(platformSettings.correlationKeyGenerator())
        .withDispatcher(eventLoopDispatcher).configure());
  }
}
