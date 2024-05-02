package cloud.bangover.platform.domain.functions.search.query.spec;

import cloud.bangover.platform.domain.functions.search.query.Pagination;
import cloud.bangover.platform.domain.functions.search.query.data.SearchData;
import cloud.bangover.platform.domain.store.AskSpecification;
import cloud.bangover.platform.domain.store.MockAskContext;
import java.util.Collection;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MockSearchDataSpec
    implements AskSpecification<MockAskContext<SearchData>, SearchData> {
  private final String discriminator;

  @Override
  public Collection<SearchData> ask(MockAskContext<SearchData> context, Pagination pagination) {
    context.applyQuery(searchData -> searchData.getDiscriminator().equals(discriminator));
    context.applySorter(Comparator.comparing(SearchData::getId));
    return context.getData();
  }
}
