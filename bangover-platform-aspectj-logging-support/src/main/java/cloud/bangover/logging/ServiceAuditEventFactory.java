package cloud.bangover.logging;

import cloud.bangover.errors.ErrorDescriptor;
import cloud.bangover.logging.audit.ServiceAuditEvent;
import java.util.Arrays;
import org.aspectj.lang.reflect.MethodSignature;

public class ServiceAuditEventFactory {
  private final String[] auditParameters;
  private final Level level;
  private final LogMessageFactory messageFactory;

  public ServiceAuditEventFactory(MethodSignature signature, Object[] arguments,
      ServiceAudit annotation) {
    super();
    this.auditParameters = annotation.auditParameters();
    this.messageFactory = new MethodLogMessageFactory(signature, arguments, annotation.message());
    this.level = annotation.level();
  }

  public ServiceAuditEventFactory(MethodSignature signature, ErrorDescriptor errorDescriptor) {
    super();
    this.level = Level.ERROR;
    this.auditParameters = new String[0];
    this.messageFactory = new ErrorLogMessageFactory(signature, errorDescriptor);
  }

  public ServiceAuditEvent createAuditEvent() {
    return new ServiceAuditEvent(level, messageFactory.createLogMessageText(),
        Arrays.asList(auditParameters));
  }
}
