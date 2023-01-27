package cloud.bangover.events;

import cloud.bangover.BoundedContextId;
import cloud.bangover.interactions.pubsub.LocalPubSub;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GlobalEventsTest {
  private static final BoundedContextId BOUNDED_CONTEXT = BoundedContextId.createFor("CTX");
  private static final EventType<Object> EVENT_TYPE = EventType.createFor("ECHO");
  private static final Object ECHO_OBJECT = new Object();

  @Test
  public void shouldNotReactOnEventsIfItWasNotSetUp() {
    // Given
    MockEventListener<Object> listener = new MockEventListener<>();
    EventPublisher<Object> publisher = GlobalEvents.publisher(BOUNDED_CONTEXT, EVENT_TYPE);
    EventBus.EventSubscribtion subscribtion =
        GlobalEvents.subscribeOn(BOUNDED_CONTEXT, EVENT_TYPE, listener);
    // When
    publisher.publish(ECHO_OBJECT);
    subscribtion.unsubscribe();
    // Then
    Assert.assertFalse(listener.getHistory().hasEntries());
  }

  @Test
  public void shouldReactOnEventsIfItIsSetUp() {
    // Given
    GlobalEvents.configureEventBus(PubSubEventBus.factory(new LocalPubSub<>()).createEventBus());
    MockEventListener<Object> listener = new MockEventListener<>();
    EventPublisher<Object> publisher = GlobalEvents.publisher(BOUNDED_CONTEXT, EVENT_TYPE);
    EventBus.EventSubscribtion subscribtion =
        GlobalEvents.subscribeOn(BOUNDED_CONTEXT, EVENT_TYPE, listener);
    // When
    publisher.publish(ECHO_OBJECT);
    subscribtion.unsubscribe();
    // Then
    Assert.assertTrue(listener.getHistory().hasEntry(0, ECHO_OBJECT));
  }

  @After
  public void tearDown() {
    GlobalEvents.reset();
  }
}
