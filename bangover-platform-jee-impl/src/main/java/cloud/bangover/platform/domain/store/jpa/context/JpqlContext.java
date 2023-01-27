package cloud.bangover.platform.domain.store.jpa.context;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.jpa.JpaContext;
import lombok.NonNull;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class JpqlContext<I, E extends Entity<I>> extends JpaContext<E> {
  private String jpqlQuery = "";

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public JpqlContext(@NonNull Class entityType, @NonNull EntityManager entityManager) {
    super(entityType, entityManager);
  }

  public void applyJpqlQuery(String jpqlQuery) {
    this.jpqlQuery = jpqlQuery;
  }

  @Override
  protected TypedQuery<E> createQuery(Class<E> entityType, EntityManager entityManager) {
    return entityManager.createQuery(jpqlQuery, entityType);
  }
}
