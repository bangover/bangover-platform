package cloud.bangover.platform.domain.functions.search;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.platform.domain.functions.search.ApplyFiltersFunction.FiltersEntity;
import cloud.bangover.platform.domain.functions.search.ApplyFiltersFunction.FiltersEntityStore;
import cloud.bangover.platform.domain.store.MockEntityStore;

import java.util.Optional;

public class MockFiltersEntityStore<F, E extends FiltersEntity<F>> extends MockEntityStore<Long, E>
    implements FiltersEntityStore<F, E> {

  @Override
  public Optional<E> findByFilters(F filters) {
    return CollectionWrapper.of(getState()).find(entity -> filters.equals(entity.getFilters()));
  }
}
