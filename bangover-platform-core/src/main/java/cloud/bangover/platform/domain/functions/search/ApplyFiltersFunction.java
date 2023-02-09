package cloud.bangover.platform.domain.functions.search;

import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.generators.Generator;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.EntityStore;

import java.util.Optional;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplyFiltersFunction<F, E extends ApplyFiltersFunction.FiltersEntity<F>> implements
    BusinessFunction<F, Long> {
  private final Generator<Long> tokenGenerator;
  private final FiltersEntityStore<F, E> filtersStore;
  private final FiltersEntityCreator<F, E> filtersEntityCreator;

  
  
  @Override
  public void invoke(Context<F, Long> context) {
    F filters = context.getRequest();
    E entity = obtainRegisteredFiltersEntity(filters);
    context.reply(entity.getId());
  }

  private E obtainRegisteredFiltersEntity(F filters) {
    return filtersStore.findByFilters(filters).orElseGet(() -> {
      E entity = filtersEntityCreator.createFilters(tokenGenerator.generateNext(), filters);
      filtersStore.save(entity);
      return entity;
    });
  }

  public interface FiltersEntity<F> extends Entity<Long> {
    F getFilters();
  }

  public interface FiltersEntityCreator<F, E extends FiltersEntity<F>> {
    E createFilters(Long token, F filters);
  }

  public static interface FiltersEntityStore<F, E extends FiltersEntity<F>> extends
      EntityStore<Long, E> {
    Optional<E> findByFilters(F filters);

    default <D> FiltersEntityStore<D, FiltersEntity<D>> derive(
        Function<D, F> forwardFiltersConverter,
        FiltersEntityCreator<F, E> originalFiltersEntityCreator,
        Function<F, D> backwardFiltersConverter) {

      FiltersEntityCreator<D, FiltersEntity<D>> adaptedStoreCreator =
          (id, filters) -> new GenericFiltersEntity<D>(id, filters);

      return this.derive(forwardFiltersConverter, originalFiltersEntityCreator,
          backwardFiltersConverter, adaptedStoreCreator);
    }

    default <D, C extends FiltersEntity<D>> FiltersEntityStore<D, C> derive(
        Function<D, F> forwardFiltersConverter,
        FiltersEntityCreator<F, E> originalFiltersEntityCreator,
        Function<F, D> backwardFiltersConverter,
        FiltersEntityCreator<D, C> adaptedFiltersEntityCreator) {

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
        public Optional<C> findByFilters(D filters) {
          F derivedFilters = forwardFiltersConverter.apply(filters);
          return originalStore.findByFilters(derivedFilters).map(backwardEntityConverter);
        }
      };
    }
  }
}
