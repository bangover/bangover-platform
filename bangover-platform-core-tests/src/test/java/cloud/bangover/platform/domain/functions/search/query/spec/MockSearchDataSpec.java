package cloud.bangover.platform.domain.functions.search.query.spec;

import cloud.bangover.platform.domain.functions.search.query.data.SearchData;
import cloud.bangover.platform.domain.store.MockDataStore;
import cloud.bangover.platform.domain.store.Specification;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
public class MockSearchDataSpec implements Specification<MockDataStore.MockContext<SearchData>> {
  private final String discriminator;

  @Override
  public void applyTo(MockDataStore.MockContext<SearchData> context) {
    context.applyQuery(searchData -> searchData.getDiscriminator().equals(discriminator));
    context.applySorter(Comparator.comparing(SearchData::getId));
  }
}
