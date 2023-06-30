package cloud.bangover.logging;

import cloud.bangover.errors.ErrorDescriptor;
import org.aspectj.lang.reflect.MethodSignature;

class LogRecordFactory {
  private final Level level;
  private final LogMessageFactory logMessageFactory;

  public LogRecordFactory(MethodSignature signature, Object[] arguments, LogMethod annotation) {
    super();
    this.level = annotation.level();
    this.logMessageFactory =
        new MethodLogMessageFactory(signature, arguments, annotation.message());
  }
  
  public LogRecordFactory(MethodSignature signature, ErrorDescriptor descriptor) {
    super();
    this.level = Level.ERROR;
    this.logMessageFactory = new ErrorLogMessageFactory(signature, descriptor);
  }

  public LogRecord createLogMessage() {
    return new LogRecord(level, logMessageFactory.createLogMessageText());
  }
}