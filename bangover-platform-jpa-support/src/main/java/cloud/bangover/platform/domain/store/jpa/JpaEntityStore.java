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
    return Optional.ofNullable(entityManager.find(entityType, id));
  }

  @Override
  public void save(E entity) {
    unitOfWork.executeWorkUnit(() -> {
      Optional<E> foundEntity = find(entity.getId());
      if (foundEntity.isPresent()) {
        entityManager.lock(foundEntity.get(), lockMode);
        entityManager.merge(entity);
      } else {
        entityManager.persist(entity);
      }
    });
  }

  @Override
  public void delete(I id) {
    unitOfWork.executeWorkUnit(() -> {
      find(id).ifPresent(entity -> {
        entityManager.lock(entity, lockMode);
        entityManager.remove(entity);
      });
    });
  }
}
