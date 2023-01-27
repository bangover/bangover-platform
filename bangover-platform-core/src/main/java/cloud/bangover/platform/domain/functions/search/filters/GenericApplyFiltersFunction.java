package cloud.bangover.platform.domain.functions.search.filters;

import cloud.bangover.generators.Generator;

public class GenericApplyFiltersFunction<F extends Filters> extends ApplyFiltersFunction<F, GenericFiltersEntity<F>> {
    @SuppressWarnings("unchecked")
    public GenericApplyFiltersFunction(
            Generator<Long> tokenGenerator,
            FiltersEntityStore<F, FiltersEntity<F>> filtersStore
    ) {
        super(tokenGenerator, (FiltersEntityStore<F, GenericFiltersEntity<F>>)(Object)filtersStore,
              GenericFiltersEntity::new
        );
    }
}
