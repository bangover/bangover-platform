package cloud.bangover.platform.domain.events;

import cloud.bangover.events.GlobalEvents;
import cloud.bangover.platform.domain.EventDrivenEntity;
import cloud.bangover.platform.domain.store.EntityStore;

public abstract class EventDrivenEntitySynchronizingEventListener<I,
    E extends EventDrivenEntity<I, E>, V> extends EntitySynchronizingEventListener<I, E, V> {

  protected EventDrivenEntitySynchronizingEventListener(
      SynchronizingEntitiesProvider<I, E, V> synchronizingEntitiesProvider,
      EntityStore<I, E> entityStore, EntitySynchronizer<I, E, V> entitySynchronizer) {
    super(synchronizingEntitiesProvider, entityStore, entitySynchronizer);
  }

  @Override
  protected void processEntity(E entity, V event) {
    super.processEntity(entity, event);
    entity.getLocalEventsManager().getEventsToPublish().forEach(descriptor -> {
      GlobalEvents.publisher(descriptor.getEventType()).publish(descriptor.getEvent());
    });
  }
}
