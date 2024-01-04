package cloud.bangover.validation;

import cloud.bangover.errors.ErrorDescriptor.ErrorCode;
import cloud.bangover.errors.ValidationException;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionDecorator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationFunction<Q, S> implements BusinessFunction<Q, S> {
  public static final ErrorCode VALIDATION_ERROR_CODE = ErrorCode.createFor(-2L);

  private final BusinessFunction<Q, S> decorated;
  private final ValidationService validationService;

  /**
   * Create function decorators
   *
   * @param validationService The validation service
   * @return The decorator
   */
  public static final BusinessFunctionDecorator decorator(ValidationService validationService) {
    return new BusinessFunctionDecorator() {
      @Override
      public <Q, S> BusinessFunction<Q, S> decorate(BusinessFunction<Q, S> original) {
        return new ValidationFunction<>(original, validationService);
      }
    };
  }

  @Override
  public void invoke(Context<Q, S> context) {
    ValidationState validationState = validationService.validate(context.getRequest());
    if (validationState.isValid()) {
      decorated.invoke(context);
    } else {
      context.reject(new ValidationException(VALIDATION_ERROR_CODE, validationState));
    }
  }
}
