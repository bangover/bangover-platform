package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.functions.search.Pagination;

import java.util.Collection;

public interface DataStore<C, V> {

  Collection<V> find(Specification<C> specification);

  Collection<V> find(Specification<C> specification, Pagination pagination);
}
