package cloud.bangover.platform.domain.functions;

import cloud.bangover.BoundedContextId;
import cloud.bangover.errors.ApplicationException;
import lombok.NonNull;

public class EntityNotFoundException extends ApplicationException {
  private static final long serialVersionUID = -7365353337897209344L;

  public EntityNotFoundException() {
    this(BoundedContextId.PLATFORM_CONTEXT, ErrorCode.UNRECOGNIZED_ERROR_CODE);
  }

  public EntityNotFoundException(@NonNull BoundedContextId contextId,
      @NonNull ErrorCode errorCode) {
    this(contextId, ErrorSeverity.BUSINESS, errorCode);
  }

  public EntityNotFoundException(@NonNull BoundedContextId contextId,
      @NonNull ErrorSeverity errorSeverity, @NonNull ErrorCode errorCode) {
    super(contextId, errorSeverity, errorCode);
  }
}
