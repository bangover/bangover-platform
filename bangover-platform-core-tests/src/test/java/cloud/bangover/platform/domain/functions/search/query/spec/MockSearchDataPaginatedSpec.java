package cloud.bangover.platform.domain.functions.search.query.spec;

import cloud.bangover.platform.domain.functions.search.query.Pagination;
import lombok.Getter;

@Getter
public class MockSearchDataPaginatedSpec extends MockSearchDataSpec {
  private final Pagination pagination;

  public MockSearchDataPaginatedSpec(String discriminator, Pagination pagination) {
    super(discriminator);
    this.pagination = pagination;
  }
}
