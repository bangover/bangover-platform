package cloud.bangover.platform.domain.functions.search;

import cloud.bangover.async.promises.Promise;
import cloud.bangover.dataset.DataSet;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.MockFunctionRunner;
import cloud.bangover.generators.StubGenerator;
import cloud.bangover.platform.domain.functions.search.ApplyFiltersFunction.FiltersData;
import cloud.bangover.platform.domain.functions.search.ApplyFiltersFunction.FiltersEntity;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ApplyFiltersFunctionTest {
  private static final Long TOKEN = 1L;
  private static final MockFilters COMMAND = new MockFilters(100L);

  @Test
  public void shouldFiltersBeRetrievedIfItHasAlreadyBeenCreated() throws Throwable {
    // Given
    StubGenerator<Long> tokenGenerator = new StubGenerator<>(TOKEN);
    MockFiltersEntityStore<Long, FiltersEntity<Long>> filtersStore = new MockFiltersEntityStore<>();
    GenericFiltersEntity<Long> entity = new GenericFiltersEntity<>(TOKEN, COMMAND.createFilters());
    filtersStore.loadDataSet(DataSet.of(Arrays.asList(entity)));
    BusinessFunction<FiltersData<Long>, Long> function =
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
    MockFiltersEntityStore<Long, FiltersEntity<Long>> filtersStore = new MockFiltersEntityStore<>();
    BusinessFunction<FiltersData<Long>, Long> function =
        new GenericApplyFiltersFunction<>(tokenGenerator, filtersStore);
    // When
    Promise<Long> promise = MockFunctionRunner.createFor(function).executeFunction(COMMAND);
    // Then
    Assert.assertEquals(TOKEN, promise.get(10L));
    FiltersEntity<Long> filter = filtersStore.getDataSet().toList().get(0);
    Assert.assertEquals(COMMAND.createFilters(), filter.getFilters());
    Assert.assertEquals(TOKEN, filter.getId());
  }

  @RequiredArgsConstructor
  private static class MockFilters implements FiltersData<Long> {
    private final Long command;

    @Override
    public Long createFilters() {
      return this.command;
    }
  }
}
