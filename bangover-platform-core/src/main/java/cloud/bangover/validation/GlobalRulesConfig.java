package cloud.bangover.validation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(staticName = "create")
public class GlobalRulesConfig {
  @Getter
  private Map<String, Rule<?>> candidates = new HashMap<>();

  public <T> GlobalRulesConfig withCandidate(String name, Rule<T> rule) {
    candidates.put(name, rule);
    return this;
  }
}
