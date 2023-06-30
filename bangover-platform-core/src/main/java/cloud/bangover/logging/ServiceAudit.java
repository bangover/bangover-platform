package cloud.bangover.logging;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(RUNTIME)
public @interface ServiceAudit {
  String contextId() default "";

  String[] auditParameters() default {};

  Level level() default Level.INFO;

  String message() default "";
}
