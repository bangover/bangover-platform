package cloud.bangover.platform.domain;

import cloud.bangover.BoundedContextId;
import cloud.bangover.errors.ApplicationException;

public class UnacceptableEntityCommandException extends ApplicationException {
  private static final long serialVersionUID = -7346425581709863863L;
  public static final ErrorCode UNACCEPTABLE_ENTITY_COMMAND_ERROR_CODE = ErrorCode.createFor(-4L);

  public UnacceptableEntityCommandException(BoundedContextId boundedContext, Object entityId,
      Object commandId) {
    super(boundedContext, ErrorSeverity.INCIDENT, UNACCEPTABLE_ENTITY_COMMAND_ERROR_CODE,
        String.format(
            "Operation is intended for entity[id=%s], but it is executed on entity[id=%s].",
            commandId, entityId));
  }
}
