package cloud.bangover.platform.domain.store.jpa;

import cloud.bangover.platform.domain.functions.search.Pagination;
import cloud.bangover.platform.domain.store.DataStore;
import cloud.bangover.platform.domain.store.Specification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

@RequiredArgsConstructor
public class JpaDataStore<V> implements DataStore<JpaContext<V>, V> {
  @NonNull
  private final Class<V> entityType;
  @NonNull
  private final EntityManager entityManager;
  @NonNull
  private final JpaContext.JpaContextFactory<V> contextCreator;

  @Override
  public Collection<V> find(Specification<JpaContext<V>> specification) {
    return createQuery(specification).getResultList();
  }

  @Override
  public Collection<V> find(Specification<JpaContext<V>> specification, Pagination pagination) {
    TypedQuery<V> query = createQuery(specification);
    query.setFirstResult(pagination.getOffset().intValue());
    query.setMaxResults(pagination.getSize());
    return query.getResultList();
  }

  private TypedQuery<V> createQuery(Specification<JpaContext<V>> specification) {
    JpaContext<V> context = contextCreator.createContext(entityType, entityManager);
    specification.applyTo(context);
    return context.getQuery();
  }
}
