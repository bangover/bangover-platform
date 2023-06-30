package cloud.bangover.platform.domain.functions.search.filters;

public interface FiltersEntityCreator<F extends Filters, E extends FiltersEntity<F>> {
    E createFilters(Long token, F filters);
}
