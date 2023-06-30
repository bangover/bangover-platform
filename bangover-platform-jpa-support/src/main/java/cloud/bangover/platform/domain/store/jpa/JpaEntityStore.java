package cloud.bangover.platform.domain.store.jpa;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;
import cloud.bangover.transactions.UnitOfWork;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaEntityStore<I, E extends Entity<I>> implements EntityStore<I, E> {
  @NonNull
  private final Class<E> entityType;
  @NonNull
  private final EntityManager entityManager;
  @NonNull
  private final UnitOfWork unitOfWork;

  @Override
  public Optional<E> find(I id) {
    return Optional.ofNullable(entityManager.find(entityType, id));
  }

  @Override
  public void save(E entity) {
    unitOfWork.executeWorkUnit(() -> entityManager.merge(entity));
  }

  @Override
  public void delete(I id) {
    unitOfWork.executeWorkUnit(() -> find(id).ifPresent(entityManager::remove));
  }
}
