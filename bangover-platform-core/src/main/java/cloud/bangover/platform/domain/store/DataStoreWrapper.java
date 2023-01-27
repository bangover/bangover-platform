package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.functions.search.query.Pagination;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataStoreWrapper<C, V> implements DataStore<C, V> {
  private final DataStore<C, V> wrappedStore;

  @Override
  public Collection<V> find(Specification<C> specification) {
    return wrappedStore.find(specification);
  }

  @Override
  public Collection<V> find(Specification<C> specification, Pagination pagination) {
    return wrappedStore.find(specification, pagination);
  }
}
