package cloud.bangover.platform.domain.functions.search;

import cloud.bangover.platform.domain.functions.search.filters.FiltersEntity;
import cloud.bangover.platform.domain.functions.search.filters.FiltersEntityStore;
import cloud.bangover.platform.domain.functions.search.filters.Filters;
import cloud.bangover.platform.domain.store.MockEntityStore;

import java.util.Optional;

public class MockFiltersEntityStore<F extends Filters, E extends FiltersEntity<F>>
    extends MockEntityStore<Long, E> implements FiltersEntityStore<F, E> {

  @Override
  public Optional<E> findFiltersByToken(String token) {
    return getState().stream().filter(entity -> entity.getFilters().tokenize().equals(token))
        .findFirst();
  }
}
