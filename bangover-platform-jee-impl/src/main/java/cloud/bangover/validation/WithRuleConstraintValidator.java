package cloud.bangover.validation;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.text.Text;
import cloud.bangover.text.TextTemplates;
import cloud.bangover.validation.global.GlobalValidations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class WithRuleConstraintValidator implements ConstraintValidator<WithRule, Object> {
  private String ruleAlias;

  @Override
  public void initialize(WithRule constraintAnnotation) {
    this.ruleAlias = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    Rule<Object> rule = GlobalValidations.getRule(ruleAlias);
    if (rule.isAcceptableFor(value)) {
      return validateAcceptableValue(value, context, rule);
    }
    return true;
  }

  private boolean validateAcceptableValue(Object value, ConstraintValidatorContext context,
      Rule<Object> rule) {
    CollectionWrapper<String> errors = interpolateErrorMessages(rule.check(value));
    if (errors.count() > 0) {
      appendInterpolatedViolationMessages(context, errors);
      return false;
    }
    return true;
  }

  private void appendInterpolatedViolationMessages(ConstraintValidatorContext context,
      CollectionWrapper<String> errors) {
    context.disableDefaultConstraintViolation();
    errors.forEach(
        error -> context.buildConstraintViolationWithTemplate(error).addConstraintViolation());
  }

  CollectionWrapper<String> interpolateErrorMessages(Collection<ErrorMessage> errorMessages) {
    return CollectionWrapper.of(errorMessages)
        .map(message -> TextTemplates.createBy(message.getMessage(), message.getParameters()))
        .map(Text::interpolate);
  }
}
