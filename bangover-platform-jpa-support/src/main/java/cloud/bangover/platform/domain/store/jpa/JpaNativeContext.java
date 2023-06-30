package cloud.bangover.platform.domain.store.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import lombok.NonNull;

public class JpaNativeContext<V> extends JpaContext<V> {
  private String sqlQuery = "";
  private String resultSetMappingName = "";

  public JpaNativeContext(@NonNull Class<V> entityType, @NonNull EntityManager entityManager) {
    super(entityType, entityManager);
  }

  public void applySqlQuery(String sqlQuery) {
    this.sqlQuery = sqlQuery;
  }

  public void applyResultSetMapping(String resultSetMappingName) {
    this.resultSetMappingName = resultSetMappingName;
  }

  @Override
  protected Query createQuery(Class<V> entityType, EntityManager entityManager) {
    return entityManager.createNativeQuery(sqlQuery, resultSetMappingName);
  }
}
