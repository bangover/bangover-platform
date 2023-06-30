package cloud.bangover.platform.domain.functions.search.filters;

import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.generators.Generator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplyFiltersFunction<F extends Filters, E extends FiltersEntity<F>>
        implements BusinessFunction<FiltersData<F>, Long> {
    private final Generator<Long> tokenGenerator;
    private final FiltersEntityStore<F, E> filtersStore;
    private final FiltersEntityCreator<F, E> filtersEntityCreator;

    @Override
    public void invoke(Context<FiltersData<F>, Long> context) {
        F filters = context.getRequest().createFilters();
        E entity = obtainRegisteredFiltersEntity(filters);
        context.reply(entity.getId());
    }

    private E obtainRegisteredFiltersEntity(F filters) {
        return filtersStore.findFiltersByToken(filters.tokenize()).orElseGet(() -> {
            E entity = filtersEntityCreator.createFilters(tokenGenerator.generateNext(), filters);
            filtersStore.save(entity);
            return entity;
        });
    }
}
