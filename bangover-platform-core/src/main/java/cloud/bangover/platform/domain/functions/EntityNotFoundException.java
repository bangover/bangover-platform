package cloud.bangover.platform.domain.functions;

import cloud.bangover.errors.ApplicationException;
import lombok.NonNull;

public class EntityNotFoundException extends ApplicationException {
  private static final long serialVersionUID = -7365353337897209344L;
  public static final ErrorCode ENTITY_NOT_FOUND_ERROR_CODE = ErrorCode.createFor(-3L);

  public EntityNotFoundException() {
    this(ENTITY_NOT_FOUND_ERROR_CODE);
  }

  public EntityNotFoundException(@NonNull ErrorCode errorCode) {
    this(ErrorSeverity.BUSINESS, errorCode);
  }

  public EntityNotFoundException(@NonNull ErrorSeverity errorSeverity,
      @NonNull ErrorCode errorCode) {
    super(errorSeverity, errorCode);
  }
}
