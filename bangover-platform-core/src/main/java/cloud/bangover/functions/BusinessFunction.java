package cloud.bangover.functions;

/**
 * This interface describes the functional unit, encapsulating one of completed features, valuable
 * for business. This concept is directed to replace application services components by them. So it
 * should lead to in the projects will exist only services in the domain layer and application
 * services rules role will be perform business functions.
 *
 * @param <Q> The request type name
 * @param <S> The response type name
 */
public interface BusinessFunction<Q, S> {
  void invoke(Context<Q, S> context);

  /**
   * Make the {@link BusinessFunction} components cascade. Each cascaded function will be take
   * response of previous function as an argument.
   * 
   * @param <T>      The cascaded {@link BusinessFunction} response type name
   * @param cascaded The cascaded {@link BusinessFunction}
   * @return The derived {@link BusinessFunction}, calling the functions cascade.
   */
  default <T> BusinessFunction<Q, T> cascade(BusinessFunction<S, T> cascaded) {
    return new BusinessFunctionsCascade<Q, S, T>(this, cascaded);
  }

  /**
   * The business function object.
   *
   * @param <Q> The request type name
   * @param <S> The response type name
   */
  interface Context<Q, S> {
    Q getRequest();

    default void reply() {
      reply(null);
    }

    void reply(S response);

    void reject(Throwable error);
  }
}
