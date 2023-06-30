package cloud.bangover.platform.domain.functions.search.filters;

public interface FiltersData<F extends Filters> {
    F createFilters();
}
