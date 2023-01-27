package cloud.bangover.platform.domain.store.jpa.context;

import cloud.bangover.platform.domain.store.jpa.JpaContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import lombok.NonNull;

public class CriteriaContext<V> extends JpaContext<V> {
  private CriteriaQueryCreator<V> criteriaQueryCreator =
      ((entityType, criteriaBuilder) -> criteriaBuilder.createQuery(entityType));

  public CriteriaContext(@NonNull Class<V> entityType, @NonNull EntityManager entityManager) {
    super(entityType, entityManager);
  }

  @Override
  protected TypedQuery<V> createQuery(Class<V> entityType, EntityManager entityManager) {
    CriteriaQuery<V> criteriaQuery =
        criteriaQueryCreator.createQuery(entityType, entityManager.getCriteriaBuilder());
    return entityManager.createQuery(criteriaQuery);
  }

  public interface CriteriaQueryCreator<V> {
    CriteriaQuery<V> createQuery(Class<V> entityType, CriteriaBuilder criteriaBuilder);
  }
}
