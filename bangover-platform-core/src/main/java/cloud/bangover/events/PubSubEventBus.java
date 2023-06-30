package cloud.bangover.events;

import cloud.bangover.BoundedContextId;
import cloud.bangover.interactions.pubsub.PubSub;
import cloud.bangover.interactions.pubsub.Subscriber;
import cloud.bangover.interactions.pubsub.Subscribtion;
import cloud.bangover.interactions.pubsub.Topic;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * This class implements event bus over pub-sub interaction.
 *
 * @author Dmitry Mikhaylenko
 *
 */
public class PubSubEventBus implements EventBus {
  private static final Topic BROADCAST_TOPIC_NAME = Topic.ofName("PUB.SUB.BROADCAST");

  private final PubSub<Object> pubSubChannel;

  public static final Factory factory(PubSub<Object> channel) {
    return () -> new PubSubEventBus(channel);
  }

  private PubSubEventBus(PubSub<Object> pubSubChannel) {
    super();
    this.pubSubChannel = pubSubChannel;
    this.subscribeOnAll(new PubSubBroadcastEventRetranslator());
  }

  @Override
  public <E> EventPublisher<E> getPublisher(BoundedContextId contextId, EventType<E> eventType) {
    return new PubSubBroadcastPublisher<E>(contextId, eventType);
  }

  @Override
  public <E> EventSubscribtion subscribeOn(BoundedContextId contextId, EventType<E> eventType,
      EventListener<E> eventListener) {
    Topic topic = createTopic(contextId, eventType);
    Subscriber<E> eventSubscriber = new PubSubEventSubscriber<>(eventType, eventListener);
    return subscribeOn(topic, eventType.getEventClass(), eventSubscriber);
  }

  @Override
  public EventSubscribtion subscribeOnAll(GlobalEventListener globalListener) {
    Subscriber<BroadcastEvent> broadcastSubscriber =
        new PubSubBroadcastEventSubscriber(globalListener);
    return subscribeOn(BROADCAST_TOPIC_NAME, BroadcastEvent.class, broadcastSubscriber);
  }

  private <E> EventSubscribtion subscribeOn(Topic topic, Class<E> type, Subscriber<E> subscriber) {
    Subscribtion subscribtion = pubSubChannel.subscribeOn(topic, type, subscriber);
    return new PubSubEventBusSubscribtion(subscribtion);
  }

  private <E> Topic createTopic(BoundedContextId contextId, EventType<E> eventType) {
    return Topic.ofName(String.format("%s__%s", contextId, eventType.extract()));
  }

  private final <E> void checkThatTheEventIsAcceptable(EventType<E> eventType, Object event) {
    if (!eventType.isAccepts(event)) {
      throw new UnacceptableEventException(event, eventType);
    }
  }

  @Value
  private class BroadcastEvent {
    private final BoundedContextId contextId;
    private final EventType<Object> eventType;
    private final Object event;
  }

  @RequiredArgsConstructor
  private class PubSubBroadcastPublisher<E> implements EventPublisher<E> {
    private final BoundedContextId contextId;
    private final EventType<E> eventType;

    @Override
    @SuppressWarnings("unchecked")
    public void publish(E event) {
      pubSubChannel.getPublisher(BROADCAST_TOPIC_NAME)
          .publish(new BroadcastEvent(contextId, (EventType<Object>) eventType, event));
    }
  }

  @RequiredArgsConstructor
  public class PubSubBroadcastEventSubscriber implements Subscriber<BroadcastEvent> {
    private final GlobalEventListener globalEventListener;

    @Override
    public void onMessage(BroadcastEvent message) {
      globalEventListener.onEvent(new EventDescriptor<>(message.getContextId(),
          message.getEventType(), message.getEvent()));
    }
  }

  private class PubSubBroadcastEventRetranslator implements GlobalEventListener {
    @Override
    public void onEvent(EventDescriptor<Object> eventDescriptor) {
      checkThatTheEventIsAcceptable(eventDescriptor.getEventType(), eventDescriptor.getEvent());
      pubSubChannel
          .getPublisher(
              createTopic(eventDescriptor.getBoundedContext(), eventDescriptor.getEventType()))
          .publish(eventDescriptor.getEvent());
    }
  }

//  @RequiredArgsConstructor
//  private class PubSubEventBusPublisher<E> implements EventPublisher<E> {
//    private final BoundedContextId contextId;
//    private final EventType<E> eventType;
//
//    @Override
//    public void publish(E event) {
//      checkThatTheEventIsAcceptable(eventType, event);
//      pubSubChannel.getPublisher(createTopic(contextId, eventType)).publish(event);
//    }
//  }

  /**
   * This exception is happened if the event instance is not accepted to the event type.
   *
   * @author Dmitry Mikhaylenko
   *
   */
  public static final class UnacceptableEventException extends RuntimeException {
    private static final long serialVersionUID = -7206353691833222090L;

    public UnacceptableEventException(Object instance, EventType<?> eventType) {
      super(String.format("The event instance [%s] is not accepted for the [%s] acceptable type",
          instance.getClass(), eventType));
    }
  }

  @RequiredArgsConstructor
  private final class PubSubEventSubscriber<E> implements Subscriber<E> {
    private final EventType<E> eventType;
    private final EventListener<E> eventListener;

    @Override
    public void onMessage(E message) {
      if (eventType.isAccepts(message)) {
        eventListener.onEvent(message);
      }
    }
  }

  @RequiredArgsConstructor
  private final class PubSubEventBusSubscribtion implements EventSubscribtion {
    private final Subscribtion pubSubSubscribtion;

    @Override
    public void unsubscribe() {
      pubSubSubscribtion.unsubscribe();
    }
  }
}
