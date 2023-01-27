package cloud.bangover.platform.domain.store.jpa;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaEntityStore<I, E extends Entity<I>> implements EntityStore<I, E> {
  @NonNull
  private final Class<E> entityType;
  @NonNull
  private final EntityManager entityManager;

  @Override
  public Optional<E> find(I id) {
    return Optional.ofNullable(entityManager.find(entityType, id));
  }

  @Override
  public void save(E entity) {
    entityManager.merge(entity);
  }

  @Override
  public void delete(I id) {
    find(id).ifPresent(entityManager::remove);
  }
}
