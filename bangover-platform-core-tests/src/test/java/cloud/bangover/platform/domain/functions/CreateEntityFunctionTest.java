package cloud.bangover.platform.domain.functions;

import cloud.bangover.async.promises.Promise;
import cloud.bangover.errors.ErrorDescriptor;
import cloud.bangover.errors.UnexpectedErrorException;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.functions.registry.DefaultBusinessFunctionExecutor;
import cloud.bangover.generators.StubGenerator;
import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.platform.domain.store.MockEntityStore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class CreateEntityFunctionTest {
  private static final String DEFAULT_DESCRIPTION = "N/A";
  private static final String DESCRIPTION_REQUEST = "Entity description";

  private StubGenerator<Long> generator = new StubGenerator<>();
  private MockEntityStore<Long, MockEntity> store = new MockEntityStore<>();
  private BusinessFunctionRegistry registry = new DefaultBusinessFunctionExecutor();

  @Before
  public void setUp() {
    store.clear();
  }

  @After
  public void tearDown() {
    store.clear();
  }

  @Test
  public void shouldCreateEntityWithoutParameters() throws Throwable {
    // Given
    generator.configure().withNextEntry(1L);
    BusinessFunction<Void, Long> function =
        new CreateEntityFunction<>(store, () -> new MockEntity(generator));
    ReplyOnlyInteractor<Long> interactor = registry.registerReplyOnlyFunction(Long.class, function);
    // When
    Promise<Long> result = interactor.invoke();
    // Then
    Assert.assertEquals((Long) 1L, result.get(10L));
    List<MockEntity> entities = store.getDataSet().toList();
    ;
    Assert.assertEquals(1, entities.size());
    MockEntity entity = entities.get(0);
    Assert.assertEquals((Long) 1L, entity.getId());
    Assert.assertEquals(DEFAULT_DESCRIPTION, entity.getDescription());
  }

  @Test
  public void shouldCreateEntityWithParameter() throws Throwable {
    // Given
    generator.configure().withNextEntry(1L);
    BusinessFunction<String, Long> function = new CreateEntityFunction<Long, MockEntity, String>(
        store, request -> new MockEntity(generator, request));
    RequestReplyInteractor<String, Long> interactor =
        registry.registerRequestReplyFunction(String.class, Long.class, function);
    // When
    Promise<Long> result = interactor.invoke(DESCRIPTION_REQUEST);
    // Then
    Assert.assertEquals((Long) 1L, result.get(10L));
    List<MockEntity> entities = store.getDataSet().toList();
    Assert.assertEquals(1, entities.size());
    MockEntity entity = entities.get(0);
    Assert.assertEquals((Long) 1L, entity.getId());
    Assert.assertEquals(DESCRIPTION_REQUEST, entity.getDescription());
  }

  @Test
  public void shouldCreateEntityWithFault() {
    // Given
    generator.configure().withNextEntry(1L);
    BusinessFunction<String, Long> function =
        new CreateEntityFunction<Long, MockEntity, String>(store, request -> {
          throw new RuntimeException("ERROR");
        });
    RequestReplyInteractor<String, Long> interactor =
        registry.registerRequestReplyFunction(String.class, Long.class, function);
    // When
    Promise<Long> result = interactor.invoke(DESCRIPTION_REQUEST);
    // Then
    UnexpectedErrorException error = Assert.assertThrows(UnexpectedErrorException.class, () -> {
      result.get(10L);
      Assert.fail();
    });
    Assert.assertEquals(ErrorDescriptor.ErrorCode.UNRECOGNIZED_ERROR_CODE, error.getErrorCode());
    Assert.assertEquals(ErrorDescriptor.ErrorSeverity.INCIDENT, error.getErrorSeverity());
    Assert.assertEquals(0, store.getDataSet().toList().size());
  }
}
