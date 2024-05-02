package cloud.bangover.platform.domain.events;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.EntityEvent;
import cloud.bangover.platform.domain.store.EntityStore;
import cloud.bangover.transactions.UnitOfWork;
import java.util.Arrays;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

public class EntityEventSynchronizingEventListener<I, E extends Entity<I>,
    V extends EntityEvent<I, E, V>> extends EntitySynchronizingEventListener<I, E, V> {

  public EntityEventSynchronizingEventListener(EntityStore<I, E> entityStore,
      EntitySynchronizer<I, E, V> entitySynchronizer, UnitOfWork unitOfWork) {
    super(new EntityEventEntityProvider<>(entityStore), entityStore, entitySynchronizer,
        unitOfWork);
  }

  public EntityEventSynchronizingEventListener(EntityStore<I, E> entityStore,
      EntitySynchronizer<I, E, V> entitySynchronizer) {
    super(new EntityEventEntityProvider<>(entityStore), entityStore, entitySynchronizer);
  }

  @RequiredArgsConstructor
  private static class EntityEventEntityProvider<I, E extends Entity<I>,
      V extends EntityEvent<I, E, V>> implements SynchronizingEntitiesProvider<I, E, V> {
    private final EntityStore<I, E> entityStore;

    @Override
    public Collection<E> findBy(V event) {
      return Arrays.asList(event.getEntity(entityStore));
    }
  }
}
