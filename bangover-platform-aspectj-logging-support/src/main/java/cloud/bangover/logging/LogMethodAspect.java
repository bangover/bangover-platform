package cloud.bangover.logging;

import cloud.bangover.errors.ApplicationException;
import cloud.bangover.errors.UnexpectedErrorException;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LogMethodAspect {
  @Pointcut(value = "@annotation(logMethodAnnotation) && execution(* *(..))")
  public void loggableMethodPointcut(LogMethod logMethodAnnotation) {
  }

  @Around(value = "loggableMethodPointcut(logMethodAnnotation)")
  public Object logMethodInvocation(ProceedingJoinPoint joinPoint, LogMethod logMethodAnnotation)
      throws Throwable {
    JoinPointIntrospector joinPointIntrospector = JoinPointIntrospector.createFor(joinPoint);
    Optional<MethodSignature> signature = joinPointIntrospector.getSignature(MethodSignature.class);
    try {
      logInvokedMethod(signature, joinPoint.getArgs(), logMethodAnnotation);
      return joinPoint.proceed();
    } catch (ApplicationException error) {
      logApplicationException(signature, error);
      throw error;
    } catch (Exception error) {
      logException(signature, error);
      throw error;
    }
  }

  private void logInvokedMethod(Optional<MethodSignature> signature, Object[] arguments,
      LogMethod annotation) {
    signature.ifPresent(value -> {
      ApplicationLogger appLogger = Loggers.applicationLogger(value.getDeclaringType());
      LogRecordFactory messageFactory = new LogRecordFactory(value, arguments, annotation);
      appLogger.log(messageFactory.createLogMessage());
    });
  }

  private void logException(Optional<MethodSignature> signature, Exception exception) {
    logApplicationException(signature, new UnexpectedErrorException(exception));
  }

  private void logApplicationException(Optional<MethodSignature> signature,
      ApplicationException error) {
    signature.ifPresent(value -> {
      ApplicationLogger appLogger = Loggers.applicationLogger(value.getDeclaringType());
      LogRecordFactory messageFactory = new LogRecordFactory(value, error);
      appLogger.log(messageFactory.createLogMessage());
    });
  }
}
