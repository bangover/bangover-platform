package cloud.bangover.platform.domain.functions.search.query.spec;

import cloud.bangover.platform.domain.functions.search.query.PaginatedQuery;
import cloud.bangover.platform.domain.functions.search.query.Pagination;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.Optional;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PaginatedSearchQuery extends SearchQuery implements PaginatedQuery {
  private final Pagination pagination = new Pagination(Optional.of(0L), Optional.of(3));

  public PaginatedSearchQuery(String discriminator) {
    super(discriminator);
  }
}
