package cloud.bangover.platform.domain.functions.search.filters;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The filters entity, keeping the search parameters.
 *
 * @param <F> The filter object type (It may be designed as a value object or entity)
 */
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenericFiltersEntity<F extends Filters> implements FiltersEntity<F> {
    @EqualsAndHashCode.Include
    private Long id;
    private F filters;

    @Builder
    public GenericFiltersEntity(Long token, F filters) {
        this.id = token;
        this.filters = filters;
    }
}