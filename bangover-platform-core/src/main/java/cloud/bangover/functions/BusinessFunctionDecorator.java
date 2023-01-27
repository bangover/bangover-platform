package cloud.bangover.functions;

/**
 * This interface describes the component, which decorate business function by additional logic. It
 * allows to implement high-order decorator function pattern for business functions.
 */
public interface BusinessFunctionDecorator {
  /**
   * Decorate business function.
   *
   * @param original The original business function
   * @param <Q>      Request data type
   * @param <S>      Response data type
   * @return The decorated business function
   */
  <Q, S> BusinessFunction<Q, S> decorate(BusinessFunction<Q, S> original);
}
