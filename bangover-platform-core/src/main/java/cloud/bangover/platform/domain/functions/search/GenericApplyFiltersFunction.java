package cloud.bangover.platform.domain.functions.search;

import cloud.bangover.generators.Generator;

public class GenericApplyFiltersFunction<F> extends
    ApplyFiltersFunction<F, GenericFiltersEntity<F>> {

  @SuppressWarnings("unchecked")
  public GenericApplyFiltersFunction(Generator<Long> tokenGenerator,
      FiltersEntityStore<F, GenericFiltersEntity<F>> filtersStore) {
    super(tokenGenerator, filtersStore, GenericFiltersEntity::new);
  }
}
