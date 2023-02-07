package cloud.bangover.functions;

/**
 * This interface describes the component, which decorate business function by additional logic. It
 * allows to implement high-order decorator function pattern for business functions.
 */
public interface BusinessFunctionDecorator {

  /**
   * Make cascade of the {@link BusinessFunctionDecorator} components. The cascaded decorator will
   * be applied before decorator on which method
   * {@link BusinessFunctionDecorator#cascade(BusinessFunctionDecorator)} is called.
   * 
   * @param cascaded The cascaded {@link BusinessFunctionDecorator}
   * @return The derived {@link BusinessFunctionDecorator} applying decorators cascade.
   */
  default BusinessFunctionDecorator cascade(BusinessFunctionDecorator cascaded) {
    return new BusinessFunctionDecoratorsCascade(this, cascaded);
  }

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
