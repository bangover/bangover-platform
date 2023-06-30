package cloud.bangover.logging;

import cloud.bangover.errors.ApplicationException;
import cloud.bangover.errors.UnexpectedErrorException;
import cloud.bangover.logging.audit.ServiceAuditLogger;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ServiceAuditAspect {
  @Pointcut(value = "@annotation(serviceAuditAnnotation) && execution(* *(..))")
  public void serviceAuditPointcut(ServiceAudit serviceAuditAnnotation) {
  }

  @Around(value = "serviceAuditPointcut(serviceAuditAnnotation)")
  public Object serviceAuditInvocation(ProceedingJoinPoint joinPoint,
      ServiceAudit serviceAuditAnnotation) throws Throwable {
    JoinPointIntrospector joinPointIntrospector = JoinPointIntrospector.createFor(joinPoint);
    Optional<MethodSignature> signature = joinPointIntrospector.getSignature(MethodSignature.class);
    try {
      auditInvokedMethod(signature, joinPoint.getArgs(), serviceAuditAnnotation);
      return joinPoint.proceed();
    } catch (ApplicationException error) {
      auditApplicationException(signature, error);
      throw error;
    } catch (Exception error) {
      auditException(signature, error);
      throw error;
    }
  }

  private void auditInvokedMethod(Optional<MethodSignature> signature, Object[] arguments,
      ServiceAudit annotation) {
    signature.ifPresent(value -> {
      ServiceAuditLogger auditLogger = Loggers.eventsLogger();
      ServiceAuditEventFactory eventFactory =
          new ServiceAuditEventFactory(value, arguments, annotation);
      auditLogger.log(eventFactory.createAuditEvent());
    });
  }

  private void auditException(Optional<MethodSignature> signature, Exception exception) {
    auditApplicationException(signature, new UnexpectedErrorException(exception));
  }

  private void auditApplicationException(Optional<MethodSignature> signature,
      ApplicationException error) {
    signature.ifPresent(value -> {
      ServiceAuditLogger auditLogger = Loggers.eventsLogger();
      ServiceAuditEventFactory eventFactory = new ServiceAuditEventFactory(value, error);
      auditLogger.log(eventFactory.createAuditEvent());
    });
  }
}
