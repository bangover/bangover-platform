package cloud.bangover.platform.domain.store.jpa;

import cloud.bangover.platform.domain.store.jpa.parameters.Parameters;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@RequiredArgsConstructor
public abstract class JpaContext<V> {
  @NonNull
  private final Class<V> entityType;
  @NonNull
  private final EntityManager entityManager;
  @NonNull
  private Parameters parameters = new Parameters();

  public final void applyParameters(@NonNull Parameters parameters) {
    this.parameters = parameters;
  }

  public TypedQuery<V> getQuery() {
    TypedQuery<V> query = createQuery(entityType, entityManager);
    parameters.apply(query);
    return query;
  }

  protected abstract TypedQuery<V> createQuery(Class<V> entityType, EntityManager entityManager);

  public interface JpaContextFactory<C extends JpaContext<V>, V> {
    C createContext(Class<V> entityType, EntityManager entityManager);
  }
}
