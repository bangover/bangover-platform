package cloud.bangover.async.promises;

import cloud.bangover.actors.ActorSystem;
import cloud.bangover.actors.Actors;
import cloud.bangover.actors.CorrelationKeyGenerator;
import cloud.bangover.actors.FixedMessagesWaitingDispatcher;
import cloud.bangover.actors.SingleThreadDispatcher;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ActorSystemBasedDeferredFunctionRunnerTest {
  private static final Object ECHO_OBJECT = new Object();

  private ActorSystem actorSystem;

  @Before
  public void setUp() {
    this.actorSystem = Actors.create(systemConfigurer -> systemConfigurer
        .withCorrelationKeyGenerator(new CorrelationKeyGenerator("INSTANCE"))
        .withDispatcher(new FixedMessagesWaitingDispatcher(1, new SingleThreadDispatcher()))
        .configure());
    Promises.DeferredFunctionRunner deferredFunctionRunner =
        ActorSystemBasedDeferredFunctionRunner.createFor(actorSystem);
    Promises.configureDeferredFunctionRunner(deferredFunctionRunner);
    actorSystem.start();
  }

  @Test
  public void shouldResolvePromise() throws Throwable {
    // Given
    Deferred.DeferredFunction<Object> deferredFunction = deferred -> deferred.resolve(ECHO_OBJECT);
    // When
    Promise<Object> promise = Promises.of(deferredFunction);
    // Then
    Assert.assertSame(ECHO_OBJECT, promise.get(100L));
  }

  @After
  public void tearDown() {
    actorSystem.shutdown();
    Promises.configureDeferredFunctionRunner(
        new ExecutorServiceBasedDeferredFunctionRunner(Executors::newSingleThreadExecutor));
  }
}
