package cloud.bangover.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import cloud.bangover.BoundedContextId;
import cloud.bangover.errors.ErrorDescriptor;
import cloud.bangover.errors.ValidationException;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionDecorator;
import cloud.bangover.functions.MockFunctionRunner;
import cloud.bangover.functions.MockFunctionRunner.Result;

@RunWith(JUnit4.class)
public class ValidationFunctionTest {
  private static final BoundedContextId CONTEXT_ID = BoundedContextId.createFor("CTX");
  private static final ErrorDescriptor.ErrorCode ERROR_CODE =
      ErrorDescriptor.ErrorCode.createFor(1L);
  private static final BusinessFunction<String, String> ECHO_FUNCTION =
      context -> context.reply(context.getRequest());
  private static final ValidationState INVALID_STATE =
      new ValidationState().withUngrouped(ErrorMessage.createFor("ERR"));

  @Test
  public void shouldPassForValidRequest() throws Throwable {
    // Given
    ValidationService validationService = new StubValidationService();
    BusinessFunctionDecorator decorator =
        ValidationFunction.decorator(CONTEXT_ID, ERROR_CODE, validationService);
    BusinessFunction<String, String> decorated = decorator.decorate(ECHO_FUNCTION);

    // When

    Result<String> result = MockFunctionRunner.createFor(decorated).executeFunction("HELLO");
    // Then
    Assert.assertEquals("HELLO", result.getResult());
  }

  @Test
  public void shouldThrowValidationExceptionIfRequestInvalid() {
    // Given
    ValidationService validationService = new StubValidationService(INVALID_STATE);
    BusinessFunctionDecorator decorator =
        ValidationFunction.decorator(CONTEXT_ID, ERROR_CODE, validationService);
    BusinessFunction<String, String> decorated = decorator.decorate(ECHO_FUNCTION);
    // When
    ValidationException error = Assert.assertThrows(ValidationException.class, () -> {
      MockFunctionRunner.createFor(decorated).executeFunction("HELLO").getResult();
    });

    // Then
    Assert.assertEquals(CONTEXT_ID, error.getContextId());
    Assert.assertEquals(ERROR_CODE, error.getErrorCode());
    Assert.assertEquals(INVALID_STATE.getErrorState(), error.getErrorState());
  }
}
