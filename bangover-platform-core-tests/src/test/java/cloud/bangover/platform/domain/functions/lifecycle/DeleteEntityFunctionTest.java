package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.async.promises.Promise;
import cloud.bangover.dataset.DataSet;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.functions.registry.DefaultBusinessFunctionExecutor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.platform.domain.functions.EntityLifecycleFunction.EntityLifecycleCommand;
import cloud.bangover.platform.domain.functions.EntityNotFoundException;
import cloud.bangover.platform.domain.functions.MockEntity;
import cloud.bangover.platform.domain.store.MockEntityStore;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeleteEntityFunctionTest {
  private static final String DESCRIPTION_REQUEST = "Entity description";
  private static final MockEntity MOCK_ENTITY = new MockEntity(1L, DESCRIPTION_REQUEST);

  private MockEntityStore<Long, MockEntity> store = new MockEntityStore<>();
  private BusinessFunctionRegistry registry = new DefaultBusinessFunctionExecutor();

  @Before
  public void setUp() {
    store.loadDataSet(DataSet.of(Arrays.asList(MOCK_ENTITY)));
  }

  @After
  public void tearDown() {
    store.clear();
  }

  @Test
  public void shouldEntityBeDeleted() throws Throwable {
    // Given
    BusinessFunction<MockEntityDeleteCommand, Void> function = new DeleteEntityFunction<>(store);
    RequestReplyInteractor<MockEntityDeleteCommand, Void> interactor =
        registry.registerRequestReplyFunction(MockEntityDeleteCommand.class, Void.class, function);
    // When
    Promise<Void> result = interactor.invoke(new MockEntityDeleteCommand(1L));
    // Then
    result.then(r -> {
      List<MockEntity> entities = store.getDataSet().toList();
      Assert.assertEquals(0, entities.size());
    }).error(error -> Assert.fail(error.getMessage())).await(10L);
  }

  @Test
  public void shouldDeleteOperationBeFailed() throws Throwable {
    // Given
    BusinessFunction<MockEntityDeleteCommand, Void> function = new DeleteEntityFunction<>(store);
    RequestReplyInteractor<MockEntityDeleteCommand, Void> interactor =
        registry.registerRequestReplyFunction(MockEntityDeleteCommand.class, Void.class, function);
    // When
    Promise<Void> result = interactor.invoke(new MockEntityDeleteCommand(2L));
    // Then
    result.then(r -> Assert.fail()).error(error -> {
      Assert.assertTrue(error instanceof EntityNotFoundException);
      List<MockEntity> entities = store.getDataSet().toList();
      Assert.assertEquals(1, entities.size());
    }).await(10L);
  }

  @Getter
  @RequiredArgsConstructor
  private class MockEntityDeleteCommand implements EntityLifecycleCommand<Long> {
    private final Long id;
  }
}
