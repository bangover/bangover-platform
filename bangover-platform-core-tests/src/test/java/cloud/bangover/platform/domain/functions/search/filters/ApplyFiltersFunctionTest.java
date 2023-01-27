package cloud.bangover.platform.domain.functions.search.filters;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import cloud.bangover.dataset.DataSet;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.MockFunctionRunner;
import cloud.bangover.functions.MockFunctionRunner.Result;
import cloud.bangover.generators.StubGenerator;
import cloud.bangover.platform.domain.functions.search.MockFiltersEntityStore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RunWith(JUnit4.class)
public class ApplyFiltersFunctionTest {
  private static final Long TOKEN = 1L;
  private static final MockFiltersData COMMAND = new MockFiltersData(100L);

  @Test
  public void shouldFiltersBeRetrievedIfItHasAlreadyBeenCreated() throws Throwable {
    // Given
    StubGenerator<Long> tokenGenerator = new StubGenerator<>(TOKEN);
    MockFiltersEntityStore<MockFilters, FiltersEntity<MockFilters>> filtersStore =
        new MockFiltersEntityStore<>();
    GenericFiltersEntity<MockFilters> entity =
        new GenericFiltersEntity<>(TOKEN, COMMAND.createFilters());
    filtersStore.loadDataSet(DataSet.of(Arrays.asList(entity)));
    BusinessFunction<FiltersData<MockFilters>, Long> function =
        new GenericApplyFiltersFunction<>(tokenGenerator, filtersStore);
    // When
    Result<Long> promise = MockFunctionRunner.createFor(function).executeFunction(COMMAND);
    // Then
    Assert.assertEquals(TOKEN, promise.getResult());
    Assert.assertEquals(DataSet.of(Arrays.asList(entity)), filtersStore.getDataSet());
  }

  @Test
  public void shouldFiltersBeRetrievedIfItHasNotBeenCreated() throws Throwable {
    // Given
    StubGenerator<Long> tokenGenerator = new StubGenerator<>(TOKEN);
    MockFiltersEntityStore<MockFilters, FiltersEntity<MockFilters>> filtersStore =
        new MockFiltersEntityStore<>();
    BusinessFunction<FiltersData<MockFilters>, Long> function =
        new GenericApplyFiltersFunction<>(tokenGenerator, filtersStore);
    // When
    Result<Long> promise = MockFunctionRunner.createFor(function).executeFunction(COMMAND);
    // Then
    Assert.assertEquals(TOKEN, promise.getResult());
    FiltersEntity<MockFilters> filter = filtersStore.getDataSet().toList().get(0);
    Assert.assertEquals(COMMAND.createFilters(), filter.getFilters());
    Assert.assertEquals(TOKEN, filter.getId());
  }

  @RequiredArgsConstructor
  private static class MockFiltersData implements FiltersData<MockFilters> {
    private final Long command;

    @Override
    public MockFilters createFilters() {
      return new MockFilters(this.command);
    }
  }

  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  private static class MockFilters implements Filters {
    private final Long command;

    @Override
    public String tokenize() {
      return String.format("%s", command);
    }
  }
}
