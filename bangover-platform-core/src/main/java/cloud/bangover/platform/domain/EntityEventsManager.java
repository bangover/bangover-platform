package cloud.bangover.platform.domain;

import cloud.bangover.events.EventDescriptor;
import cloud.bangover.events.EventPublisher;
import cloud.bangover.events.EventType;
import cloud.bangover.events.LocalEventBus;
import java.util.Collection;
import java.util.LinkedList;
import lombok.Getter;

public class EntityEventsManager<I, E extends Entity<I>> {
  private final LocalEventBus eventBus;
  @Getter
  private final Collection<EventDescriptor<Object>> eventsToPublish = new LinkedList<>();

  public EntityEventsManager() {
    super();
    this.eventBus = new LocalEventBus();
    this.eventBus.subscribeOnAll(eventDescriptor -> eventsToPublish.add(eventDescriptor));
  }

  public <V extends EntityEvent<I, E, V>> void publishDomainEvent(V event) {
    getPublisher(event.getEventType()).publish(event);
  }

  private <V extends EntityEvent<I, E, V>> EventPublisher<V> getPublisher(EventType<V> eventType) {
    return eventBus.getPublisher(eventType);
  }
}