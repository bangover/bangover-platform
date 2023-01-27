package cloud.bangover.functions;

public class IncompatibleTypeException extends RuntimeException {
  private static final long serialVersionUID = 748712603823284354L;

  public IncompatibleTypeException(Class<?> expectedType, Object object) {
    super(String.format("Object %s is not compatible to type %s", object, expectedType));
  }
}
