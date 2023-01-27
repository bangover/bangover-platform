package cloud.bangover.platform.config;

import cloud.bangover.actors.ActorSystem;
import cloud.bangover.actors.Actors;
import cloud.bangover.actors.CorrelationKey;
import cloud.bangover.actors.CorrelationKeyGenerator;
import cloud.bangover.actors.EventLoop;
import cloud.bangover.actors.ExecutorServiceDispatcher;
import cloud.bangover.generators.Generator;
import cloud.bangover.platform.PlatformSettings;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class ActorSystemConfiguration {
  @Inject
  private PlatformSettings platformSettings;

  @Produces
  @ApplicationScoped
  public Generator<CorrelationKey> correlationKeyGenerator() {
    return new CorrelationKeyGenerator(platformSettings.getInstanceId());
  }

  @Produces
  @ApplicationScoped
  public EventLoop.Dispatcher eventLoopDispatcher() {
    return ExecutorServiceDispatcher.createFor(eventLoopExecutorService());
  }

  @Produces
  @ApplicationScoped
  public ActorSystem actorSystem(Generator<CorrelationKey> correlationKeyGenerator,
      EventLoop.Dispatcher eventLoopDispatcher) {
    return Actors.create(
        systemConfigurer -> systemConfigurer.withCorrelationKeyGenerator(correlationKeyGenerator)
            .withDispatcher(eventLoopDispatcher).configure());
  }

  private ExecutorService eventLoopExecutorService() {
    return Executors.newWorkStealingPool(platformSettings.getThreadsCount());
  }
}
