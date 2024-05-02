package cloud.bangover.transactions.events;

import cloud.bangover.events.EventBus;
import cloud.bangover.events.EventBus.EventSubscribtion;
import cloud.bangover.events.EventDescriptor;
import cloud.bangover.events.EventType;
import cloud.bangover.events.GlobalEvents;
import cloud.bangover.events.LocalEventBus;
import cloud.bangover.events.MockEventListener;
import cloud.bangover.events.MockGlobalEventListener;
import cloud.bangover.transactions.UnitOfWorkExtension;
import cloud.bangover.transactions.events.UowEventPublishingExtension.PublishingOnAbortStrategy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PublishingEventsTransactionallyTest {
  private static final Object EVENT = new Object();
  private static final EventType<Object> EVENT_TYPE = EventType.createFor("SOME_EVENT");

  @Test
  public void shouldEventNotBePublishedIfTransactionStartedButNotCompletedOrAborted() {
    // given
    EventBus eventBus = new LocalEventBus();
    UowEventsPublishingController publishingController = new UowEventsPublishingController(eventBus);
    eventBus = new UowEventBusProxy(eventBus, () -> publishingController);
    GlobalEvents.configureEventBus(eventBus);
    UnitOfWorkExtension eventsPublishingExtesion =
        new UowEventPublishingExtension(() -> publishingController);
    MockEventListener<Object> eventListener = new MockEventListener<Object>();
    MockGlobalEventListener globalEventListener = new MockGlobalEventListener();
    EventSubscribtion subscription = eventBus.subscribeOn(EVENT_TYPE, eventListener);
    EventSubscribtion globalSubscribtion = eventBus.subscribeOnAll(globalEventListener);

    // when
    eventsPublishingExtesion.onStarted();
    eventBus.getPublisher(EVENT_TYPE).publish(EVENT);

    // then
    Assert.assertFalse(eventListener.getHistory().hasEntry(0, EVENT));
    Assert.assertFalse(globalEventListener.getHistory().hasEntry(0,
        new EventDescriptor<Object>(EVENT_TYPE, EVENT)));

    // cleanup
    globalSubscribtion.unsubscribe();
    subscription.unsubscribe();
    GlobalEvents.reset();
  }

  @Test
  public void shouldPublishEventAfterTransactionComplete() {
    // given
    EventBus eventBus = new LocalEventBus();
    UowEventsPublishingController publishingController = new UowEventsPublishingController(eventBus);
    eventBus = new UowEventBusProxy(eventBus, () -> publishingController);
    GlobalEvents.configureEventBus(eventBus);
    UnitOfWorkExtension eventsPublishingExtesion =
        new UowEventPublishingExtension(() -> publishingController);
    MockEventListener<Object> eventListener = new MockEventListener<Object>();
    MockGlobalEventListener globalEventListener = new MockGlobalEventListener();
    EventSubscribtion subscription = eventBus.subscribeOn(EVENT_TYPE, eventListener);
    EventSubscribtion globalSubscribtion = eventBus.subscribeOnAll(globalEventListener);

    // when
    eventsPublishingExtesion.onStarted();
    eventBus.getPublisher(EVENT_TYPE).publish(EVENT);
    eventsPublishingExtesion.onCompleted();

    // then
    Assert.assertTrue(eventListener.getHistory().hasEntry(0, EVENT));
    Assert.assertTrue(globalEventListener.getHistory().hasEntry(0,
        new EventDescriptor<Object>(EVENT_TYPE, EVENT)));

    // cleanup
    globalSubscribtion.unsubscribe();
    subscription.unsubscribe();
    GlobalEvents.reset();
  }

  @Test
  public void shouldPublishEventAfterTransactionAborted() {
    // given
    EventBus eventBus = new LocalEventBus();
    UowEventsPublishingController publishingController = new UowEventsPublishingController(eventBus);
    eventBus = new UowEventBusProxy(eventBus, () -> publishingController);
    GlobalEvents.configureEventBus(eventBus);
    UnitOfWorkExtension eventsPublishingExtesion =
        new UowEventPublishingExtension(() -> publishingController);
    MockEventListener<Object> eventListener = new MockEventListener<Object>();
    MockGlobalEventListener globalEventListener = new MockGlobalEventListener();
    EventSubscribtion subscription = eventBus.subscribeOn(EVENT_TYPE, eventListener);
    EventSubscribtion globalSubscribtion = eventBus.subscribeOnAll(globalEventListener);

    // when
    eventsPublishingExtesion.onStarted();
    eventBus.getPublisher(EVENT_TYPE).publish(EVENT);
    eventsPublishingExtesion.onAborted();

    // then
    Assert.assertTrue(eventListener.getHistory().hasEntry(0, EVENT));
    Assert.assertTrue(globalEventListener.getHistory().hasEntry(0,
        new EventDescriptor<Object>(EVENT_TYPE, EVENT)));

    // cleanup
    globalSubscribtion.unsubscribe();
    subscription.unsubscribe();
    GlobalEvents.reset();
  }
  
  @Test
  public void shouldNotToPublishEventAfterTransactionAborted() {
    // given
    EventBus eventBus = new LocalEventBus();
    UowEventsPublishingController publishingController = new UowEventsPublishingController(eventBus);
    eventBus = new UowEventBusProxy(eventBus, () -> publishingController);
    GlobalEvents.configureEventBus(eventBus);
    UnitOfWorkExtension eventsPublishingExtesion =
        new UowEventPublishingExtension(() -> publishingController, PublishingOnAbortStrategy.CANCEL);
    MockEventListener<Object> eventListener = new MockEventListener<Object>();
    MockGlobalEventListener globalEventListener = new MockGlobalEventListener();
    EventSubscribtion subscription = eventBus.subscribeOn(EVENT_TYPE, eventListener);
    EventSubscribtion globalSubscribtion = eventBus.subscribeOnAll(globalEventListener);

    // when
    eventsPublishingExtesion.onStarted();
    eventBus.getPublisher(EVENT_TYPE).publish(EVENT);
    eventsPublishingExtesion.onAborted();

    // then
    Assert.assertFalse(eventListener.getHistory().hasEntry(0, EVENT));
    Assert.assertFalse(globalEventListener.getHistory().hasEntry(0,
        new EventDescriptor<Object>(EVENT_TYPE, EVENT)));

    // cleanup
    globalSubscribtion.unsubscribe();
    subscription.unsubscribe();
    GlobalEvents.reset();
  }

  @Test
  public void shouldPublishEventOutsideOfTransactionIfTransactionWasNotStartedButWasCompletedBeforePublish() {
    // given
    EventBus eventBus = new LocalEventBus();
    UowEventsPublishingController publishingController = new UowEventsPublishingController(eventBus);
    eventBus = new UowEventBusProxy(eventBus, () -> publishingController);
    GlobalEvents.configureEventBus(eventBus);
    UnitOfWorkExtension eventsPublishingExtesion =
        new UowEventPublishingExtension(() -> publishingController);
    MockEventListener<Object> eventListener = new MockEventListener<Object>();
    MockGlobalEventListener globalEventListener = new MockGlobalEventListener();
    EventSubscribtion subscription = eventBus.subscribeOn(EVENT_TYPE, eventListener);
    EventSubscribtion globalSubscribtion = eventBus.subscribeOnAll(globalEventListener);

    // when
    eventsPublishingExtesion.onCompleted();
    eventBus.getPublisher(EVENT_TYPE).publish(EVENT);

    // then
    Assert.assertTrue(eventListener.getHistory().hasEntry(0, EVENT));
    Assert.assertTrue(globalEventListener.getHistory().hasEntry(0,
        new EventDescriptor<Object>(EVENT_TYPE, EVENT)));

    // cleanup
    globalSubscribtion.unsubscribe();
    subscription.unsubscribe();
    GlobalEvents.reset();
  }

  @Test
  public void shouldPublishEventOutsideOfTransactionIfTransactionWasNotStartedButWasAbortedBeforePublish() {
    // given
    EventBus eventBus = new LocalEventBus();
    UowEventsPublishingController publishingController = new UowEventsPublishingController(eventBus);
    eventBus = new UowEventBusProxy(eventBus, () -> publishingController);
    GlobalEvents.configureEventBus(eventBus);
    UnitOfWorkExtension eventsPublishingExtesion =
        new UowEventPublishingExtension(() -> publishingController);
    MockEventListener<Object> eventListener = new MockEventListener<Object>();
    MockGlobalEventListener globalEventListener = new MockGlobalEventListener();
    EventSubscribtion subscription = eventBus.subscribeOn(EVENT_TYPE, eventListener);
    EventSubscribtion globalSubscribtion = eventBus.subscribeOnAll(globalEventListener);

    // when
    eventsPublishingExtesion.onAborted();
    eventBus.getPublisher(EVENT_TYPE).publish(EVENT);

    // then
    Assert.assertTrue(eventListener.getHistory().hasEntry(0, EVENT));
    Assert.assertTrue(globalEventListener.getHistory().hasEntry(0,
        new EventDescriptor<Object>(EVENT_TYPE, EVENT)));

    // cleanup
    globalSubscribtion.unsubscribe();
    subscription.unsubscribe();
    GlobalEvents.reset();
  }
  
  @Test
  public void shouldPublishEventOutsideOfTransactionIfTransactionWasNotStartedButWasCompletedAfterPublish() {
    // given
    EventBus eventBus = new LocalEventBus();
    UowEventsPublishingController publishingController = new UowEventsPublishingController(eventBus);
    eventBus = new UowEventBusProxy(eventBus, () -> publishingController);
    GlobalEvents.configureEventBus(eventBus);
    UnitOfWorkExtension eventsPublishingExtesion =
        new UowEventPublishingExtension(() -> publishingController);
    MockEventListener<Object> eventListener = new MockEventListener<Object>();
    MockGlobalEventListener globalEventListener = new MockGlobalEventListener();
    EventSubscribtion subscription = eventBus.subscribeOn(EVENT_TYPE, eventListener);
    EventSubscribtion globalSubscribtion = eventBus.subscribeOnAll(globalEventListener);

    // when
    eventBus.getPublisher(EVENT_TYPE).publish(EVENT);
    eventsPublishingExtesion.onCompleted();

    // then
    Assert.assertTrue(eventListener.getHistory().hasEntry(0, EVENT));
    Assert.assertTrue(globalEventListener.getHistory().hasEntry(0,
        new EventDescriptor<Object>(EVENT_TYPE, EVENT)));

    // cleanup
    globalSubscribtion.unsubscribe();
    subscription.unsubscribe();
    GlobalEvents.reset();
  }

  @Test
  public void shouldPublishEventOutsideOfTransactionIfTransactionWasNotStartedButWasAbortedAfterPublish() {
    // given
    EventBus eventBus = new LocalEventBus();
    UowEventsPublishingController publishingController = new UowEventsPublishingController(eventBus);
    eventBus = new UowEventBusProxy(eventBus, () -> publishingController);
    GlobalEvents.configureEventBus(eventBus);
    UnitOfWorkExtension eventsPublishingExtesion =
        new UowEventPublishingExtension(() -> publishingController);
    MockEventListener<Object> eventListener = new MockEventListener<Object>();
    MockGlobalEventListener globalEventListener = new MockGlobalEventListener();
    EventSubscribtion subscription = eventBus.subscribeOn(EVENT_TYPE, eventListener);
    EventSubscribtion globalSubscribtion = eventBus.subscribeOnAll(globalEventListener);

    // when
    eventBus.getPublisher(EVENT_TYPE).publish(EVENT);
    eventsPublishingExtesion.onAborted();

    // then
    Assert.assertTrue(eventListener.getHistory().hasEntry(0, EVENT));
    Assert.assertTrue(globalEventListener.getHistory().hasEntry(0,
        new EventDescriptor<Object>(EVENT_TYPE, EVENT)));

    // cleanup
    globalSubscribtion.unsubscribe();
    subscription.unsubscribe();
    GlobalEvents.reset();
  }
}
