package cloud.bangover.platform.domain.store.jpa;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;
import cloud.bangover.transactions.UnitOfWork;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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
  @NonNull
  private final LockModeType lockMode;

  public JpaEntityStore(@NonNull Class<E> entityType, @NonNull EntityManager entityManager,
      @NonNull UnitOfWork unitOfWork) {
    this(entityType, entityManager, unitOfWork, LockModeType.NONE);
  }

  @Override
  public Optional<E> find(I id) {
    return find(id, LockModeType.NONE);
  }

  @Override
  public void save(E entity) {
    unitOfWork.executeWorkUnit(() -> {
      if (find(entity.getId(), lockMode).isPresent()) {
        entityManager.merge(entity);
      } else {
        entityManager.persist(entity);
      }
    });
  }

  @Override
  public void delete(I id) {
    unitOfWork.executeWorkUnit(() -> {
      find(id, lockMode).ifPresent(entityManager::remove);
    });
  }

  private Optional<E> find(I id, LockModeType lockType) {
    return Optional.ofNullable(entityManager.find(entityType, id, lockType));
  }
}
