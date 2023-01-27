package cloud.bangover.platform.config.lifecycle;

import cloud.bangover.validation.GlobalRulesConfigurer;
import cloud.bangover.validation.Rule;
import cloud.bangover.validation.global.GlobalValidations;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped
public class GlobalValidationConfigurer {
  @Inject
  private Instance<GlobalRulesConfigurer> configurations;

  public void registerGlobalValidations() {
    for (GlobalRulesConfigurer configuration : configurations) {
      Map<String, Rule<?>> rules = configuration.configureGlobalValidations().getCandidates();
      for (Map.Entry<String, Rule<?>> ruleEntry : rules.entrySet()) {
        GlobalValidations.registerRule(ruleEntry.getKey(), ruleEntry.getValue());
      }
    }
  }

  public void destroyGlobalValidations() {
    GlobalValidations.clear();
  }
}
