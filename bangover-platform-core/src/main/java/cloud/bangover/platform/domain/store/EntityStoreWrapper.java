package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.Entity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * This class is the basic implementation of the {@link EntityStore} wrapper. Basic functional does
 * the delegation only. If you want to add some functional you should override the method.
 * 
 * @param <I> The entity identifier type name
 * @param <E> The entity type name (must implement the {@link Entity interface})
 */
@RequiredArgsConstructor
public class EntityStoreWrapper<I, E extends Entity<I>> implements EntityStore<I, E> {
  private final EntityStore<I, E> wrappedStore;

  @Override
  public Optional<E> find(I id) {
    return wrappedStore.find(id);
  }

  @Override
  public void save(E entity) {
    wrappedStore.save(entity);
  }

  @Override
  public void delete(I id) {
    wrappedStore.delete(id);
  }
}
