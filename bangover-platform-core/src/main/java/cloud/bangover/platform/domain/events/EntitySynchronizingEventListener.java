package cloud.bangover.platform.domain.events;

import cloud.bangover.events.EventListener;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;
import cloud.bangover.transactions.UnitOfWork;
import cloud.bangover.transactions.UnitOfWorkContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EntitySynchronizingEventListener<I, E extends Entity<I>, V>
    implements EventListener<V> {
  private final SynchronizingEntitiesProvider<I, E, V> synchronizingEntitiesProvider;
  private final EntityStore<I, E> entityStore;
  private final EntitySynchronizer<I, E, V> entitySynchronizer;
  private final UnitOfWork unitOfWork;

  public EntitySynchronizingEventListener(
      SynchronizingEntitiesProvider<I, E, V> synchronizingEntitiesProvider,
      EntityStore<I, E> entityStore, EntitySynchronizer<I, E, V> entitySynchronizer) {
    this(synchronizingEntitiesProvider, entityStore, entitySynchronizer, new NullUnitOfWork());
  }

  @Override
  public void onEvent(V event) {
    unitOfWork.executeWorkUnit(() -> {
      synchronizingEntitiesProvider.findBy(event).forEach(entity -> processEntity(entity, event));
    });
  }

  protected void processEntity(E entity, V event) {
    entitySynchronizer.synchronize(entity, event);
    entityStore.save(entity);
  }

  private static class NullUnitOfWork implements UnitOfWork {
    @Override
    public UnitOfWorkContext startWork() {
      return new UnitOfWorkContext() {
        @Override
        public void completeWork() {
        }

        @Override
        public void abortWork() {
        }
      };
    }
  }
}
