package cloud.bangover.platform.domain.store;

import cloud.bangover.events.EventDescriptor;
import cloud.bangover.events.GlobalEvents;
import cloud.bangover.platform.domain.EventDrivenEntity;
import java.util.Collection;
import java.util.Collections;

/**
 * This class decorates the {@link EntityStore} for {@link EventDrivenEntity} entities and appends
 * events republishing of the locally entity published events to the global event bus.
 * 
 * @param <I> The entity id type name
 * @param <E> The entity type name (must implement the {@link EventDrivenEntity} interface)
 */
public class EventDrivenEntityStoreWrapper<I, E extends EventDrivenEntity<I, E>>
    extends EntityStoreWrapper<I, E> {

  public EventDrivenEntityStoreWrapper(EntityStore<I, E> wrappedStore) {
    super(wrappedStore);
  }

  @Override
  public void save(E entity) {
    Collection<EventDescriptor<Object>> publishedEvents =
        entity.getLocalEventsManager().getEventsToPublish();
    super.save(entity);
    republishLocallyPublishedEntityEvents(publishedEvents);
  }

  @Override
  public void delete(I id) {
    Collection<EventDescriptor<Object>> publishedEvents =
        find(id).map(entity -> entity.getLocalEventsManager().getEventsToPublish())
            .orElse(Collections.emptyList());
    super.delete(id);
    republishLocallyPublishedEntityEvents(publishedEvents);
  }

  private void republishLocallyPublishedEntityEvents(
      Collection<EventDescriptor<Object>> entityEvents) {
    entityEvents.forEach(eventDescriptor -> {
      GlobalEvents.publisher(eventDescriptor.getEventType()).publish(eventDescriptor.getEvent());
    });
  }
}
