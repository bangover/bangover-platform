package cloud.bangover.platform.config;

import cloud.bangover.validation.CombiningValidationService;
import cloud.bangover.validation.ValidationService;
import cloud.bangover.validation.context.ValidationContext;
import cloud.bangover.validation.jsr.JsrValidationService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.Arrays;

@ApplicationScoped
public class ValidationConfiguration {
  @Inject
  private Validator validator;

  @Produces
  @ApplicationScoped
  public ValidationService validationService() {
    return new CombiningValidationService(Arrays.asList(ValidationContext.createValidationService(),
        new JsrValidationService(validator)));
  }
}
