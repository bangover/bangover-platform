package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.async.promises.Promise;
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
public class MutableEntityFunctionTest {
  private static final String DESCRIPTION_REQUEST = "Entity description";
  private static final MockEntity MOCK_ENTITY = new MockEntity(1L);

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
  public void shouldMutableOperationBeExecuted() throws Throwable {
    // Given

    BusinessFunction<EditDescriptionCommand, String> function =
        new MutableEntityFunction<Long, MockEntity, EditDescriptionCommand, String>(store,
            ((entity, command) -> {
              entity.edit(command.getDescription());
              return entity.getDescription();
            }));
    RequestReplyInteractor<EditDescriptionCommand, String> interactor =
        registry.registerRequestReplyFunction(EditDescriptionCommand.class, String.class, function);
    // When
    Promise<String> result = interactor.invoke(new EditDescriptionCommand(1L, DESCRIPTION_REQUEST));
    // Then
    Assert.assertEquals(DESCRIPTION_REQUEST, result.get(10L));
    MockEntity entity = store.getDataSet().toList().get(0);
    Assert.assertEquals((Long) 1L, entity.getId());
    Assert.assertEquals(DESCRIPTION_REQUEST, entity.getDescription());
  }

  @Test
  public void shouldMutableRequestOnlyOperationBeExecuted() throws Throwable {
    // Given
    BusinessFunction<EditDescriptionCommand, Void> function =
        new MutableEntityFunction<Long, MockEntity, EditDescriptionCommand, Void>(store,
            ((entity, command) -> {
              entity.edit(command.getDescription());
            }));
    RequestReplyInteractor<EditDescriptionCommand, Void> interactor =
        registry.registerRequestReplyFunction(EditDescriptionCommand.class, Void.class, function);
    // When
    interactor.invoke(new EditDescriptionCommand(1L, DESCRIPTION_REQUEST)).await();
    // Then
    MockEntity entity = store.getDataSet().toList().get(0);
    Assert.assertEquals((Long) 1L, entity.getId());
    Assert.assertEquals(DESCRIPTION_REQUEST, entity.getDescription());
  }

  @Test
  public void shouldMutableOperationBeFailed() {
    // Given
    BusinessFunction<EditDescriptionCommand, Void> function = new ImmutableEntityFunction<>(store,
        (EntityLifecycleFunction.RequestOnlyLifecycleOperation<Long, MockEntity,
            EditDescriptionCommand>) ((entity, command) -> entity.edit(command.getDescription())));
    RequestReplyInteractor<EditDescriptionCommand, Void> interactor =
        registry.registerRequestReplyFunction(EditDescriptionCommand.class, Void.class, function);
    // When
    Promise<Void> result = interactor.invoke(new EditDescriptionCommand(2L, DESCRIPTION_REQUEST));
    // Then
    Assert.assertThrows(EntityNotFoundException.class, () -> result.get(10L));
  }

  @Getter
  @RequiredArgsConstructor
  public static class EditDescriptionCommand
      implements EntityLifecycleFunction.EntityLifecycleCommand<Long> {
    private final Long id;
    private final String description;
  }
}
