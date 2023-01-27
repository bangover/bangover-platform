package cloud.bangover.platform.domain.functions.search.filters;

import cloud.bangover.platform.domain.Entity;

public interface FiltersEntity<F extends Filters> extends Entity<Long> {
    F getFilters();
}
