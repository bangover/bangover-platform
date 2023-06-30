package cloud.bangover.platform.domain.store.jpa.context;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import cloud.bangover.platform.domain.store.jpa.JpaContext;
import lombok.NonNull;

public class JpqlContext<V> extends JpaContext<V> {
  private String jpqlQuery = "";

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public JpqlContext(@NonNull Class entityType, @NonNull EntityManager entityManager) {
    super(entityType, entityManager);
  }

  public void applyJpqlQuery(String jpqlQuery) {
    this.jpqlQuery = jpqlQuery;
  }

  @Override
  protected TypedQuery<V> createQuery(Class<V> entityType, EntityManager entityManager) {
    return entityManager.createQuery(jpqlQuery, entityType);
  }
}
