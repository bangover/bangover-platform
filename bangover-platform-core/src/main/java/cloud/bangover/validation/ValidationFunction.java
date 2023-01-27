package cloud.bangover.validation;

import cloud.bangover.BoundedContextId;
import cloud.bangover.errors.ErrorDescriptor;
import cloud.bangover.errors.ValidationException;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionDecorator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationFunction<Q, S> implements BusinessFunction<Q, S> {
  private final BoundedContextId contextId;
  private final ErrorDescriptor.ErrorCode errorCode;
  private final BusinessFunction<Q, S> decorated;
  private final ValidationService validationService;

  /**
   * Create function decorators
   *
   * @param contextId         The bounded context id
   * @param errorCode         The error code
   * @param validationService The validation service
   * @return The decorator
   */
  public static final BusinessFunctionDecorator decorator(BoundedContextId contextId,
      ErrorDescriptor.ErrorCode errorCode, ValidationService validationService) {
    return new BusinessFunctionDecorator() {
      @Override
      public <Q, S> BusinessFunction<Q, S> decorate(BusinessFunction<Q, S> original) {
        return new ValidationFunction<>(contextId, errorCode, original, validationService);
      }
    };
  }

  @Override
  public void invoke(Context<Q, S> context) {
    ValidationState validationState = validationService.validate(context.getRequest());
    if (validationState.isValid()) {
      decorated.invoke(context);
    } else {
      context.reject(new ValidationException(contextId, errorCode, validationState));
    }
  }
}
