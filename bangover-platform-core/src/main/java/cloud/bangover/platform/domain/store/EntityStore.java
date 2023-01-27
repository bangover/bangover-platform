package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.Entity;

import java.util.Optional;

public interface EntityStore<I, E extends Entity<I>> {
  Optional<E> find(I id);

  void save(E entity);

  void delete(I id);
}
