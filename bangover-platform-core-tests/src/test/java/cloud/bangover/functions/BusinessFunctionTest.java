package cloud.bangover.functions;

import cloud.bangover.async.promises.Promise;
import cloud.bangover.async.promises.Promises;
import cloud.bangover.async.timer.Timeout;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BusinessFunctionTest {
  @Test
  public void shouldFuntionCascadeBeResolved() throws Throwable {
    // Given
    BusinessFunction<Void, String> function = createFunctionsCascade(Promises.resolvedBy(10L));
    // When
    String result = MockFunctionRunner.createFor(function).executeFunction().get(10L);
    // Then
    Assert.assertEquals("10", result);
  }
  
  @Test(expected = RuntimeException.class)
  public void shouldFuntionCascadeBeRejected() throws Throwable {
    // Given
    BusinessFunction<Void, String> function = createFunctionsCascade(Promises.rejectedBy(new RuntimeException()));
    // When
    MockFunctionRunner.createFor(function).executeFunction().get(10L);
  }

  public BusinessFunction<Void, String> createFunctionsCascade(
      Promise<Long> originalFunctionResult) {
    BusinessFunction<Void, Long> original =
        new StubBusinessFunction<Void, Long>(originalFunctionResult, Timeout.ofSeconds(3L));
    return original.cascade(context -> {
      context.reply(context.getRequest().toString());
    });
  }
}
