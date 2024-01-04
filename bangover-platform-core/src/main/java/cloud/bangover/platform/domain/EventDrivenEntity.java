package cloud.bangover.platform.domain;

import cloud.bangover.BoundedContextId;
import cloud.bangover.events.EventDescriptor;
import cloud.bangover.events.EventPublisher;
import cloud.bangover.events.EventType;
import cloud.bangover.events.LocalEventBus;
import java.util.Collection;
import java.util.LinkedList;
import lombok.Getter;

public interface EventDrivenEntity<I, E extends EventDrivenEntity<I, E>> extends Entity<I> {
  default Collection<EventDescriptor<Object>> getPublishedDomainEvents() {
    return getEntityEventsManager().getEvents();
  }
  
  default <V extends EntityEvent<I, E, V>> void publishDomainEvent(V event) {
    getEntityEventsManager().publishDomainEvent(event);
  }
  
  EntityEventsManager<I, E> getEntityEventsManager();
  
  public static class EntityEventsManager<I, E extends Entity<I>> {
    private final LocalEventBus eventBus;
    private final BoundedContextId boundedContext;
    @Getter
    private final Collection<EventDescriptor<Object>> events = new LinkedList<>();

    public EntityEventsManager(BoundedContextId boundedContext) {
      super();
      this.eventBus = new LocalEventBus();
      this.boundedContext = boundedContext;
      this.eventBus.subscribeOnAll(eventDescriptor -> events.add(eventDescriptor));
    }
    
    public <V extends EntityEvent<I, E, V>> void publishDomainEvent(V event) {
      getPublisher(event.getEventType()).publish(event);
    }
    
    private <V extends EntityEvent<I, E, V>> EventPublisher<V> getPublisher(EventType<V> eventType) {
      return eventBus.getPublisher(boundedContext, eventType);
    }
  }
}
