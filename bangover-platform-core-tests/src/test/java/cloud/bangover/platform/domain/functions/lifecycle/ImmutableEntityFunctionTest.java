package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.async.promises.Promise;
import cloud.bangover.async.promises.Promises;
import cloud.bangover.dataset.DataSet;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.functions.registry.DefaultBusinessFunctionExecutor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.platform.domain.functions.EntityLifecycleFunction;
import cloud.bangover.platform.domain.functions.EntityNotFoundException;
import cloud.bangover.platform.domain.functions.MockEntity;
import cloud.bangover.platform.domain.store.MockEntityStore;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ImmutableEntityFunctionTest {
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
  public void shouldImmutableOperationBeExecuted() throws Throwable {
    // Given
    BusinessFunction<GetDescriptionCommand,
        String> function = new ImmutableEntityFunction<>(store,
            (EntityLifecycleFunction.RequestReplyLifecycleOperation<Long, MockEntity,
                GetDescriptionCommand, String>) ((entity, command) -> entity.getDescription()));
    RequestReplyInteractor<GetDescriptionCommand, String> interactor =
        registry.registerRequestReplyFunction(GetDescriptionCommand.class, String.class, function);
    // When
    Promise<String> result = interactor.invoke(new GetDescriptionCommand(1L));
    // Then
    Assert.assertEquals(DESCRIPTION_REQUEST, result.get(10L));
  }

  @Test
  public void shouldRequestOnlyImmutableOperationBeExecuted() throws Throwable {
    // Given
    Promise<String> result = Promises.of(deferred -> {
      try {
        BusinessFunction<GetDescriptionCommand,
            String> function = new ImmutableEntityFunction<>(store,
                (EntityLifecycleFunction.RequestOnlyLifecycleOperation<Long, MockEntity,
                    GetDescriptionCommand>) ((entity, command) -> deferred
                        .resolve(entity.getDescription())));
        RequestReplyInteractor<GetDescriptionCommand, String> interactor = registry
            .registerRequestReplyFunction(GetDescriptionCommand.class, String.class, function);
        // When
        interactor.invoke(new GetDescriptionCommand(1L)).await();
      } catch (Exception error) {
        deferred.reject(error);
      }
    });

    // Then
    Assert.assertEquals(DESCRIPTION_REQUEST, result.get(10L));
  }

  @Test
  public void shouldImmutableOperationBeFailed() {
    // Given
    BusinessFunction<GetDescriptionCommand,
        String> function = new ImmutableEntityFunction<>(store,
            (EntityLifecycleFunction.RequestReplyLifecycleOperation<Long, MockEntity,
                GetDescriptionCommand, String>) ((entity, command) -> entity.getDescription()));
    RequestReplyInteractor<GetDescriptionCommand, String> interactor =
        registry.registerRequestReplyFunction(GetDescriptionCommand.class, String.class, function);
    // When
    Promise<String> result = interactor.invoke(new GetDescriptionCommand(2L));
    // Then
    Assert.assertThrows(EntityNotFoundException.class, () -> result.get(10L));
  }

  @Getter
  @RequiredArgsConstructor
  public static class GetDescriptionCommand
      implements EntityLifecycleFunction.EntityLifecycleCommand<Long> {
    private final Long id;
  }
}
