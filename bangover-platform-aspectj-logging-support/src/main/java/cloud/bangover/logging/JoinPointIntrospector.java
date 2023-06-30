package cloud.bangover.logging;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.reflect.MethodSignature;

@RequiredArgsConstructor(staticName = "createFor")
public class JoinPointIntrospector {
  private final JoinPoint joinPoint;

  public List<Object> getArguments() {
    return Arrays.asList(joinPoint.getArgs());
  }

  public <S extends Signature> Optional<S> getSignature(Class<S> signatureSubtype) {
    return Optional.of(joinPoint.getSignature()).filter(signatureSubtype::isInstance)
        .map(signatureSubtype::cast);
  }

  public <A extends Annotation> Optional<A> findFieldAnnotation(Class<A> annotationType) {
    return findAnnotation(getSignature(FieldSignature.class), this::extractField, annotationType);
  }

  public <A extends Annotation> Optional<A> findMethodAnnotation(Class<A> annotationType) {
    return findAnnotation(getSignature(MethodSignature.class), this::extractMethod, annotationType);
  }

  public <A extends Annotation> Optional<A> findConstructorAnnotation(Class<A> annotationType) {
    return findAnnotation(getSignature(ConstructorSignature.class), this::extractConstructor,
        annotationType);
  }

  private AnnotatedElement extractField(FieldSignature fieldSignature) {
    return fieldSignature.getField();
  }

  private AnnotatedElement extractMethod(MethodSignature methodSignature) {
    return methodSignature.getMethod();
  }

  private AnnotatedElement extractConstructor(ConstructorSignature constructorSignature) {
    return constructorSignature.getConstructor();
  }

  private <A extends Annotation, S extends Signature> Optional<A> findAnnotation(
      Optional<S> signature, Function<S, AnnotatedElement> elementExtractor,
      Class<A> annotationType) {
    return findAnnotation(signature.map(elementExtractor), annotationType);
  }

  private <A extends Annotation> Optional<A> findAnnotation(
      Optional<AnnotatedElement> annotatedElement, @NonFinal Class<A> annotationType) {
    return annotatedElement.flatMap(element -> {
      return Arrays.asList(element.getAnnotationsByType(annotationType)).stream().findFirst();
    });
  }

}
