package cloud.bangover.platform.domain.functions.search.filters;

import cloud.bangover.platform.domain.store.EntityStore;

import java.util.Optional;
import java.util.function.Function;

public interface FiltersEntityStore<F extends Filters, E extends FiltersEntity<F>> extends
                                                                                   EntityStore<Long, E> {
    Optional<E> findFiltersByToken(String token);

    default <D extends Filters> FiltersEntityStore<D, FiltersEntity<D>> derive(
            Function<D, F> forwardFiltersConverter,
            FiltersEntityCreator<F, E> originalFiltersEntityCreator,
            Function<F, D> backwardFiltersConverter
    ) {

        FiltersEntityCreator<D, FiltersEntity<D>> adaptedStoreCreator =
                (id, filters) -> new GenericFiltersEntity<D>(id, filters);

        return this.derive(forwardFiltersConverter, originalFiltersEntityCreator,
                           backwardFiltersConverter, adaptedStoreCreator
        );
    }

    default <D extends Filters, C extends FiltersEntity<D>> FiltersEntityStore<D, C> derive(
            Function<D, F> forwardFiltersConverter,
            FiltersEntityCreator<F, E> originalFiltersEntityCreator,
            Function<F, D> backwardFiltersConverter,
            FiltersEntityCreator<D, C> adaptedFiltersEntityCreator
    ) {

        FiltersEntityStore<F, E> originalStore = this;

        Function<C, E> forwardEntityConverter = entity -> {
            F filters = forwardFiltersConverter.apply(entity.getFilters());
            return originalFiltersEntityCreator.createFilters(entity.getId(), filters);
        };

        Function<E, C> backwardEntityConverter = entity -> {
            D filters = backwardFiltersConverter.apply(entity.getFilters());
            return adaptedFiltersEntityCreator.createFilters(entity.getId(), filters);
        };

        EntityStore<Long, C> derivedBaseEntityStore =
                derive(forwardEntityConverter, backwardEntityConverter);

        return new FiltersEntityStore<D, C>() {
            @Override
            public Optional<C> find(Long id) {
                return derivedBaseEntityStore.find(id);
            }

            @Override
            public void save(C entity) {
                derivedBaseEntityStore.save(entity);
            }

            @Override
            public void delete(Long id) {
                derivedBaseEntityStore.delete(id);
            }

            @Override
            public Optional<C> findFiltersByToken(String token) {
                return originalStore.findFiltersByToken(token).map(backwardEntityConverter);
            }
        };
    }
}
