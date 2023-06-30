package cloud.bangover.functions.registry;

import cloud.bangover.BoundedContextId;
import cloud.bangover.async.promises.Promise;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.functions.BusinessFunctionRegistry.FunctionRequestReplyInteractor;
import cloud.bangover.interactions.interactor.RequestReplyInteractor;

/**
 * This class is the wrapper of {@link RequestReplyInteractor} converts it to the
 * {@link BusinessFunctionRegistry.FunctionRequestReplyInteractor}
 * 
 * @param <Q> The request type name
 * @param <S> The response type name
 * 
 * @author Dmitry Mikhaylenko
 */
public class FunctionRequestReplyInteractorWrapper<Q, S> extends FunctionMetadata
    implements FunctionRequestReplyInteractor<Q, S> {
  private final RequestReplyInteractor<Q, S> wrappedInteractor;

  public FunctionRequestReplyInteractorWrapper(BoundedContextId boundedContextId,
      RequestReplyInteractor<Q, S> wrappedInteractor) {
    super(boundedContextId);
    this.wrappedInteractor = wrappedInteractor;
  }

  @Override
  public Promise<S> invoke(Q request) {
    return wrappedInteractor.invoke(request);
  }
}
