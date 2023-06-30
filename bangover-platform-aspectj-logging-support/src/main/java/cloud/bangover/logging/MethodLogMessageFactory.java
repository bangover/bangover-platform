package cloud.bangover.logging;

import cloud.bangover.text.TextTemplate;
import cloud.bangover.text.TextTemplates;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.reflect.MethodSignature;

@RequiredArgsConstructor
class MethodLogMessageFactory implements LogMessageFactory {
  private final MethodSignature signature;
  private final Object[] arguments;
  private final String description;

  @Override
  public TextTemplate createLogMessageText() {
    return TextTemplates
        .createBy("Execute method: {{$method}}\n Description: {{$description}}"
            + "\n================\n{{$parameters}}\n================\n")
        .withParameter("$method", stringifyMethodSignature())
        .withParameter("$description", description)
        .withParameter("$parameters", stringifyParameters());
  }

  private String stringifyMethodSignature() {
    return signature.getMethod().toString();
  }

  private String stringifyParameters() {
    List<String> result = new ArrayList<String>();
    String[] parameterNames = getParametersNames();
    for (int i = 0; i < parameterNames.length; i++) {
      String parameterName = parameterNames[i];
      String parameterValue = String.valueOf(arguments[i]);
      result.add(String.format("Parameter %s: %s", parameterName, parameterValue));
    }
    return String.join("\n", result);
  }

  private String[] getParametersNames() {
    return signature.getParameterNames();
  }
}
