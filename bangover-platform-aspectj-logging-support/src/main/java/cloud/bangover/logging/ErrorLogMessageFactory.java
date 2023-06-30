package cloud.bangover.logging;

import cloud.bangover.errors.ErrorDescriptor;
import cloud.bangover.text.TextTemplate;
import cloud.bangover.text.TextTemplates;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.reflect.MethodSignature;

@RequiredArgsConstructor
class ErrorLogMessageFactory implements LogMessageFactory {
  private final MethodSignature signature;
  private final ErrorDescriptor errorDescriptor;

  @Override
  public TextTemplate createLogMessageText() {
    return TextTemplates.createBy("An exception happened: {{$errorMessage}}\n"
        + "Method: {{$signature}}\nContext id: {{$contextId}}\nError code: {{$errorCode}}\n"
        + "Error severity: {{$severity}}\n================\nStacktrace:\n{{$errorStacktrace}}\n",
        errorDescriptor.getErrorDetails())
        .withParameter("$contextId", errorDescriptor.getContextId())
        .withParameter("$errorCode", errorDescriptor.getErrorCode())
        .withParameter("$severity", errorDescriptor.getErrorSeverity())
        .withParameter("$severity", signature.getMethod().toString());
  }
}
