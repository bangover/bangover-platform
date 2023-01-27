package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.dataset.DataSet;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.functions.registry.DefaultBusinessFunctionExecutor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.platform.domain.functions.MockEntity;
import cloud.bangover.platform.domain.functions.lifecycle.SynchronizingFunction.RemoveEntityCommand;
import cloud.bangover.platform.domain.functions.lifecycle.SynchronizingFunction.SynchronizingCommand;
import cloud.bangover.platform.domain.functions.lifecycle.SynchronizingFunction.SynchronizingConverter;
import cloud.bangover.platform.domain.functions.lifecycle.SynchronizingFunction.UpdateEntityCommand;
import cloud.bangover.platform.domain.store.MockEntityStore;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SynchronizingFunctionTest {
  private static final String DESCRIPTION_REQUEST = "Entity description";
  private static final MockEntity MOCK_ENTITY = new MockEntity(1L, DESCRIPTION_REQUEST);
  private static final UpdateMockEntityData UPDATE_DATA =
      new UpdateMockEntityData(1L, DESCRIPTION_REQUEST);
  private static final RemoveMockEntityData REMOVE_DATA = new RemoveMockEntityData(1L);

  private MockEntityStore<Long, MockEntity> store = new MockEntityStore<>();
  private BusinessFunctionRegistry registry = new DefaultBusinessFunctionExecutor();

  @After
  public void tearDown() {
    store.clear();
  }

  @Test
  public void shouldPerformUpdateSynchronization() throws Throwable {
    // Given
    UpdateMockEntityConverter converter = new UpdateMockEntityConverter();
    BusinessFunction<UpdateMockEntityData, Void> function =
        new SynchronizingFunction<>(store, converter);
    RequestReplyInteractor<UpdateMockEntityData, Void> interactor =
        registry.registerRequestReplyFunction(UpdateMockEntityData.class, Void.class, function);
    // When
    interactor.invoke(UPDATE_DATA).await();
    // Then
    List<MockEntity> entities = store.getDataSet().toList();
    Assert.assertFalse(entities.isEmpty());
    MockEntity entity = entities.get(0);
    Assert.assertEquals((Long) 1L, entity.getId());
    Assert.assertEquals(DESCRIPTION_REQUEST, entity.getDescription());
  }

  @Test
  public void shouldPerformRemoveSynchronization() throws Throwable {
    // Given
    store.loadDataSet(DataSet.of(Arrays.asList(MOCK_ENTITY)));
    RemoveMockEntityConverter converter = new RemoveMockEntityConverter();
    BusinessFunction<RemoveMockEntityData, Void> function =
        new SynchronizingFunction<>(store, converter);
    RequestReplyInteractor<RemoveMockEntityData, Void> interactor =
        registry.registerRequestReplyFunction(RemoveMockEntityData.class, Void.class, function);
    // When
    interactor.invoke(REMOVE_DATA).await();
    // Then
    Assert.assertTrue(store.getDataSet().toList().isEmpty());
  }

  @RequiredArgsConstructor
  public static class UpdateMockEntityData {
    private final Long id;
    private final String description;

    public MockEntity createEntity() {
      return new MockEntity(id, description);
    }
  }

  public static class UpdateMockEntityConverter implements
      SynchronizingConverter<UpdateMockEntityData, SynchronizingCommand<Long, MockEntity>> {
    @Override
    public UpdateEntityCommand<Long, MockEntity> convert(UpdateMockEntityData data) {
      return new UpdateEntityCommand<>(data.createEntity());
    }
  }

  @Getter
  @RequiredArgsConstructor
  public static class RemoveMockEntityData {
    private final Long id;
  }

  public static class RemoveMockEntityConverter implements
      SynchronizingConverter<RemoveMockEntityData, SynchronizingCommand<Long, MockEntity>> {
    @Override
    public RemoveEntityCommand<Long, MockEntity> convert(RemoveMockEntityData data) {
      return new RemoveEntityCommand<>(data.getId());
    }
  }
}
