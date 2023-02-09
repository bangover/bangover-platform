package cloud.bangover.platform.domain.functions.search;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.platform.domain.functions.search.ApplyFiltersFunction.FiltersEntityStore;
import cloud.bangover.platform.domain.store.MockEntityStore;
import java.util.Optional;

public class MockFiltersEntityStore<F> extends MockEntityStore<Long, GenericFiltersEntity<F>>
    implements FiltersEntityStore<F, GenericFiltersEntity<F>> {

  @Override
  public Optional<GenericFiltersEntity<F>> findByFilters(F filters) {
    return CollectionWrapper.of(getState()).find(entity -> filters.equals(entity.getFilters()));
  }
}
