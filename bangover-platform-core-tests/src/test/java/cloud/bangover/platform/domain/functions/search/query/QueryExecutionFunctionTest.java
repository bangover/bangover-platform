package cloud.bangover.platform.domain.functions.search.query;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.dataset.DataSet;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.functions.registry.DefaultBusinessFunctionExecutor;
import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.platform.domain.functions.search.query.data.MockSearchDataContext;
import cloud.bangover.platform.domain.functions.search.query.data.SearchData;
import cloud.bangover.platform.domain.functions.search.query.spec.MockSearchDataSpec;
import cloud.bangover.platform.domain.functions.search.query.spec.SearchQuery;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collection;

@RunWith(JUnit4.class)
public class QueryExecutionFunctionTest {
  private static final String SEARCH_DISCRIMINATOR = "SEARCH_DISC";
  private static final String UNKNOWN_DISCRIMINATOR = "UNKNOWN_DISC";

  private final MockSearchDataContext dataStore = new MockSearchDataContext();
  private BusinessFunctionRegistry registry = new DefaultBusinessFunctionExecutor();

  @Before
  public void setUp() {
    dataStore.loadDataSet(storeDataSet());
  }

  @After
  public void tearDown() {
    dataStore.clear();
  }

  @Test
  @SneakyThrows
  public void shouldFilterElementsByDiscriminatorIndependentlyOfRequestIfSpecFactoryIsNotParameterized() {
    // Given
    BusinessFunction<Void, CollectionWrapper<SearchData>> businessFunction =
        new QueryExecutionFunction<>(dataStore, () -> new MockSearchDataSpec(SEARCH_DISCRIMINATOR));
    ReplyOnlyInteractor<CollectionWrapper<SearchData>> interactor =
        registry.registerReplyOnlyFunction(CollectionWrapper.class, businessFunction);
    // When
    CollectionWrapper<SearchData> foundData = interactor.invoke().get(100L);
    // Then
    Assert.assertTrue(foundData.containsAll(expectedSearchResult()));
  }

  @Test
  @SneakyThrows
  public void shouldFilterElementsByDiscriminatorDependentOfRequestIfSpecFactoryIsParameterized() {
    // Given
    SearchQuery searchQuery = new SearchQuery(SEARCH_DISCRIMINATOR);
    BusinessFunction<SearchQuery, CollectionWrapper<SearchData>> businessFunction =
        new QueryExecutionFunction<>(dataStore,
            query -> new MockSearchDataSpec(query.getDiscriminator()));
    RequestReplyInteractor<SearchQuery, CollectionWrapper<SearchData>> interactor = registry
        .registerRequestReplyFunction(SearchQuery.class, CollectionWrapper.class, businessFunction);
    // When
    CollectionWrapper<SearchData> foundData = interactor.invoke(searchQuery).get(100L);
    // Then
    Assert.assertTrue(foundData.containsAll(expectedSearchResult()));
  }

  private DataSet<SearchData> storeDataSet() {
    return DataSet.of(Arrays.asList(SearchData.of(1, SEARCH_DISCRIMINATOR),
        SearchData.of(2, UNKNOWN_DISCRIMINATOR), SearchData.of(3, SEARCH_DISCRIMINATOR),
        SearchData.of(4, SEARCH_DISCRIMINATOR), SearchData.of(5, UNKNOWN_DISCRIMINATOR),
        SearchData.of(6, SEARCH_DISCRIMINATOR), SearchData.of(7, UNKNOWN_DISCRIMINATOR),
        SearchData.of(8, SEARCH_DISCRIMINATOR)));
  }

  private Collection<SearchData> expectedSearchResult() {
    return Arrays.asList(SearchData.of(1, SEARCH_DISCRIMINATOR),
        SearchData.of(3, SEARCH_DISCRIMINATOR), SearchData.of(4, SEARCH_DISCRIMINATOR),
        SearchData.of(6, SEARCH_DISCRIMINATOR), SearchData.of(8, SEARCH_DISCRIMINATOR));
  }
}
