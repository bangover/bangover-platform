package cloud.bangover.events;

import cloud.bangover.BoundedContextId;
import lombok.experimental.UtilityClass;
import cloud.bangover.events.EventBus.EventSubscribtion;

@UtilityClass
public class GlobalEvents {
  private EventBus eventBus;

  static {
    reset();
  }

  public void configureEventBus(EventBus eventBus) {
    GlobalEvents.eventBus = eventBus;
  }

  public <E> EventPublisher<E> publisher(BoundedContextId contextId, EventType<E> eventType) {
    return eventBus.getPublisher(contextId, eventType);
  }

  public <E> EventSubscribtion subscribeOn(BoundedContextId contextId, EventType<E> eventType,
      EventListener<E> eventListener) {
    return eventBus.subscribeOn(contextId, eventType, eventListener);
  }

  public void reset() {
    GlobalEvents.eventBus = new NullEventBus();
  }

  private class NullEventBus implements EventBus {
    @Override
    public <E> EventPublisher<E> getPublisher(BoundedContextId contextId, EventType<E> eventType) {
      return new NullEventPublisher<>();
    }

    @Override
    public <E> EventSubscribtion subscribeOn(BoundedContextId contextId, EventType<E> eventType,
        EventListener<E> eventListener) {
      return new NullEventSubscription();
    }
  }

  private class NullEventPublisher<E> implements EventPublisher<E> {
    @Override
    public void publish(E event) {
    }
  }

  private class NullEventSubscription implements EventBus.EventSubscribtion {
    @Override
    public void unsubscribe() {
    }
  }
}
