package cloud.bangover.platform.domain.functions.search;

import cloud.bangover.async.promises.Promise;
import cloud.bangover.dataset.DataSet;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.MockFunctionRunner;
import cloud.bangover.generators.StubGenerator;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ApplyFiltersFunctionTest {
  private static final Long TOKEN = 1L;
  private static final Long COMMAND = 100L;

  @Test
  public void shouldFiltersBeRetrievedIfItHasAlreadyBeenCreated() throws Throwable {
    // Given
    StubGenerator<Long> tokenGenerator = new StubGenerator<>(TOKEN);
    MockFiltersEntityStore<Long> filtersStore = new MockFiltersEntityStore<>();
    GenericFiltersEntity<Long> entity = new GenericFiltersEntity<>(TOKEN, COMMAND);
    filtersStore.loadDataSet(DataSet.of(Arrays.asList(entity)));
    BusinessFunction<Long, Long> function =
        new GenericApplyFiltersFunction<>(tokenGenerator, filtersStore);
    // When
    Promise<Long> promise = MockFunctionRunner.createFor(function).executeFunction(COMMAND);
    // Then
    Assert.assertEquals(TOKEN, promise.get(10L));
    Assert.assertEquals(DataSet.of(Arrays.asList(entity)), filtersStore.getDataSet());
  }

  @Test
  public void shouldFiltersBeRetrievedIfItHasNotBeenCreated() throws Throwable {
    // Given
    StubGenerator<Long> tokenGenerator = new StubGenerator<>(TOKEN);
    MockFiltersEntityStore<Long> filtersStore = new MockFiltersEntityStore<>();
    BusinessFunction<Long, Long> function =
        new GenericApplyFiltersFunction<>(tokenGenerator, filtersStore);
    // When
    Promise<Long> promise = MockFunctionRunner.createFor(function).executeFunction(COMMAND);
    // Then
    Assert.assertEquals(TOKEN, promise.get(10L));
    GenericFiltersEntity<Long> filter = filtersStore.getDataSet().toList().get(0);
    Assert.assertEquals(COMMAND, filter.getFilters());
    Assert.assertEquals(TOKEN, filter.getId());
  }
}
