package cloud.bangover.functions;

import cloud.bangover.BoundedContextId;
import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;
import cloud.bangover.timer.Timeout;

public interface BusinessFunctionRegistry {
  Timeout DEFAULT_TIMEOUT = Timeout.ofSeconds(120L);

  /**
   * Register reply only business function with default timeout.
   *
   * @param responseType     The response type
   * @param businessFunction The business function
   * @param <S>              The request type name
   * @return The function {@link FunctionReplyOnlyInteractor}
   */
  default <S> FunctionReplyOnlyInteractor<S> registerReplyOnlyFunction(Class<?> responseType,
      BusinessFunction<Void, S> businessFunction) {
    return this.registerReplyOnlyFunction(responseType, businessFunction, DEFAULT_TIMEOUT);
  }

  /**
   * Register reply-only business function with custom timeout.
   *
   * @param responseType     The response type
   * @param businessFunction The business function
   * @param timeout          The timeout function
   * @param <S>              The request type name
   * @return The function {@link FunctionReplyOnlyInteractor}
   */
  <S> FunctionReplyOnlyInteractor<S> registerReplyOnlyFunction(Class<?> responseType,
      BusinessFunction<Void, S> businessFunction, Timeout timeout);

  /**
   * Register request-reply business function with default timeout.
   *
   * @param requestType      The request type
   * @param responseType     The response type
   * @param businessFunction The business function
   * @param <Q>              The request type
   * @param <S>              The response type
   * @return The function {@link FunctionRequestReplyInteractor}
   */
  default <Q, S> FunctionRequestReplyInteractor<Q, S> registerRequestReplyFunction(
      Class<?> requestType, Class<?> responseType, BusinessFunction<Q, S> businessFunction) {
    return this.registerRequestReplyFunction(requestType, responseType, businessFunction,
        DEFAULT_TIMEOUT);
  }

  /**
   * Register request-reply business function with custom timeout.
   *
   * @param requestType      The request type
   * @param responseType     The response type
   * @param businessFunction The business function
   * @param timeout          The timeout function
   * @param <Q>              The request type name
   * @param <S>              The request type name
   * @return The function {@link FunctionRequestReplyInteractor}
   */
  <Q, S> FunctionRequestReplyInteractor<Q, S> registerRequestReplyFunction(Class<?> requestType,
      Class<?> responseType, BusinessFunction<Q, S> businessFunction, Timeout timeout);

  /**
   * This interface describes the function metadata like bounded context which the function
   * associated to, etc.
   * 
   * @author Dmitry Mikhaylenko
   */
  interface FunctionMetadata {
    /**
     * Get the bounded context id
     * 
     * @return The bounded context which the function associated to.
     */
    BoundedContextId getBoundedContextId();
  }

  /**
   * This interface describes the function reply only function interactor which combines the
   * {@link FunctionMetadata} and {@link ReplyOnlyInteractor}
   * 
   * @param <S> The response type name
   */
  interface FunctionReplyOnlyInteractor<S> extends FunctionMetadata, ReplyOnlyInteractor<S> {
  }

  /**
   * This interface describes the function request-reply interactor which combines the
   * {@link FunctionMetadata} and {@link RequestReplyInteractor}
   * 
   * @param <Q> The request type name
   * @param <S> The response type name
   */
  interface FunctionRequestReplyInteractor<Q, S>
      extends FunctionMetadata, RequestReplyInteractor<Q, S> {
  }
}
