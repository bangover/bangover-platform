package cloud.bangover.platform.domain.store;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.platform.domain.functions.search.query.Pagination;

import java.util.Collection;
import java.util.function.Function;

public interface DataStore<C, V> {

  Collection<V> find(Specification<C> specification);

  Collection<V> find(Specification<C> specification, Pagination pagination);
  
  default <D> DataStore<C, D> derive(
      Function<V, D> viewConverter) {
    DataStore<C, V> store = this;
    return new DataStore<C, D>() {

      @Override
      public Collection<D> find(Specification<C> specification) {
        return CollectionWrapper.of(store.find(specification))
            .map(viewConverter)
            .get();
      }

      @Override
      public Collection<D> find(Specification<C> specification, Pagination pagination) {
        return CollectionWrapper.of(store.find(specification, pagination))
            .map(viewConverter)
            .get();
      }      
    };
  }
}
