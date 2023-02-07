package cloud.bangover.transactions;

import cloud.bangover.async.promises.Promises;
import cloud.bangover.async.timer.Timeout;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.MockFunctionRunner;
import cloud.bangover.functions.StubBusinessFunction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UnitofWorkFunctionTest {
  private static final Object OBJECT = new Object();
  private static final RuntimeException EXCEPTION = new RuntimeException();

  @Test
  public void shouldBeAbortedAfterReplying() throws Throwable {
    // Given
    BusinessFunction<Object, Object> originalFunction = context -> {
      context.reply(context.getRequest());
    };
    
    MockUnitOfWorkFunction<Object, Object> uowFunction =
        new MockUnitOfWorkFunction<>(originalFunction);
    // When
    Object result = MockFunctionRunner.createFor(uowFunction).executeFunction(OBJECT).get(10L);
    // Then
    Assert.assertTrue(uowFunction.isCompleted());
    Assert.assertSame(OBJECT, result);
  }

  @Test
  public void shouldBeAbortedAfterException() throws Exception {
    // Given
    BusinessFunction<Object, Object> originalFunction = context -> {
      throw EXCEPTION;
    };
    
    MockUnitOfWorkFunction<Object, Object> uowFunction =
        new MockUnitOfWorkFunction<>(originalFunction);
    
    // When
    RuntimeException error = Assert.assertThrows(RuntimeException.class, () -> {
      MockFunctionRunner.createFor(uowFunction).executeFunction(new Object()).get(10L);
    });

    // Then
    Assert.assertTrue(uowFunction.isAborted());
    Assert.assertSame(EXCEPTION, error);
  }

  @Test
  public void shouldBeAbortedAfterRejecting() throws Exception {
    // Given
    BusinessFunction<Object, Object> originalFunction =
        new StubBusinessFunction<>(Promises.rejectedBy(EXCEPTION), Timeout.ofSeconds(3L));
    MockUnitOfWorkFunction<Object, Object> uowFunction =
        new MockUnitOfWorkFunction<>(originalFunction);
    
    // When
    MockFunctionRunner.createFor(uowFunction).executeFunction(new Object()).await();
    
    // Then
    Assert.assertTrue(uowFunction.isAborted());
  }
}
