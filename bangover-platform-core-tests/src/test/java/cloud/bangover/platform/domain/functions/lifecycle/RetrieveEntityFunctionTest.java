package cloud.bangover.platform.domain.functions.lifecycle;

import cloud.bangover.dataset.DataSet;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.functions.registry.DefaultBusinessFunctionExecutor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.platform.domain.functions.MockEntity;
import cloud.bangover.platform.domain.store.MockEntityStore;
import java.util.Arrays;
import java.util.Optional;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RetrieveEntityFunctionTest {
  private static final String DESCRIPTION_REQUEST = "Entity description";
  private static final MockEntity MOCK_ENTITY = new MockEntity(1L, DESCRIPTION_REQUEST);

  private MockEntityStore<Long, MockEntity> store = new MockEntityStore<>();
  private BusinessFunctionRegistry registry = new DefaultBusinessFunctionExecutor();

  @After
  public void tearDown() {
    store.clear();
  }

  @Test
  public void shouldEntityBeRetrieved() throws Throwable {
    // Given
    store.loadDataSet(DataSet.of(Arrays.asList(MOCK_ENTITY)));
    BusinessFunction<Long, Optional<MockEntity>> function = new RetrieveEntityFunction<>(store);
    RequestReplyInteractor<Long, Optional<MockEntity>> interactor =
        registry.registerRequestReplyFunction(Long.class, Optional.class, function);
    // When
    MockEntity result = interactor.invoke(1L).get(10L).get();
    // Then
    Assert.assertEquals((Long) 1L, result.getId());
    Assert.assertEquals(DESCRIPTION_REQUEST, result.getDescription());
  }

  @Test
  public void shouldEmptyResultBeRetrieved() throws Throwable {
    // Given
    BusinessFunction<Long, Optional<MockEntity>> function = new RetrieveEntityFunction<>(store);
    RequestReplyInteractor<Long, Optional<MockEntity>> interactor =
        registry.registerRequestReplyFunction(Long.class, Optional.class, function);
    // When
    Optional<MockEntity> result = interactor.invoke(1L).get(10L);
    // Then
    Assert.assertFalse(result.isPresent());
  }
}
