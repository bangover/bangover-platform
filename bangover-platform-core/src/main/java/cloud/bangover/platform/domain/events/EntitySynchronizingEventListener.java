package cloud.bangover.platform.domain.events;

import cloud.bangover.events.EventListener;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EntitySynchronizingEventListener<I, E extends Entity<I>, V>
    implements EventListener<V> {
  private final SynchronizingEntitiesProvider<I, E, V> synchronizingEntitiesProvider;
  private final EntityStore<I, E> entityStore;
  private final EntitySynchronizer<I, E, V> entitySynchronizer;

  @Override
  public void onEvent(V event) {
    synchronizingEntitiesProvider.findBy(event).forEach(entity -> processEntity(entity, event));
  }

  protected void processEntity(E entity, V event) {
    entitySynchronizer.synchronize(entity, event);
    entityStore.save(entity);
  }
}
