package cloud.bangover.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = { WithRuleConstraintValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
public @interface WithRule {
  /**
   * @return The rule identifier
   */
  String value();

  /**
   * @return the error message template
   */
  String message() default "{cloud.bangover.validation.WithRule.message}";

  /**
   * @return the groups the constraint belongs to
   */
  Class<?>[] groups() default {};

  /**
   * @return the payload associated to the constraint
   */
  Class<? extends Payload>[] payload() default {};

  @Documented
  @Retention(RUNTIME)
  @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
  @interface WithRules {
    WithRule[] value();
  }
}
