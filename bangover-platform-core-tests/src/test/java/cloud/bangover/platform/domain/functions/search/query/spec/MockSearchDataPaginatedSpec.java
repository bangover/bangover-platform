package cloud.bangover.platform.domain.functions.search.query.spec;

import cloud.bangover.platform.domain.functions.search.query.Pagination;
import cloud.bangover.platform.domain.functions.search.query.data.SearchData;
import cloud.bangover.platform.domain.store.MockDataStore;

public class MockSearchDataPaginatedSpec extends MockSearchDataSpec {
  private final Pagination pagination;

  public MockSearchDataPaginatedSpec(String discriminator, Pagination pagination) {
    super(discriminator);
    this.pagination = pagination;
  }

  @Override
  public void applyTo(MockDataStore.MockContext<SearchData> context) {
    super.applyTo(context);
    context.applyPagination(pagination);
  }
}
