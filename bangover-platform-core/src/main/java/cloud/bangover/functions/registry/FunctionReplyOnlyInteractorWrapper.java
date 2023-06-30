package cloud.bangover.functions.registry;

import cloud.bangover.BoundedContextId;
import cloud.bangover.async.promises.Promise;
import cloud.bangover.functions.BusinessFunctionRegistry;
import cloud.bangover.functions.BusinessFunctionRegistry.FunctionReplyOnlyInteractor;
import cloud.bangover.interactions.interactor.ReplyOnlyInteractor;

/**
 * This class is the wrapper of {@link ReplyOnlyInteractor} converts it to the
 * {@link BusinessFunctionRegistry.FunctionReplyOnlyInteractor}
 * 
 * @param <S> The response type name
 * 
 * @author Dmitry Mikhaylenko
 */
class FunctionReplyOnlyInteractorWrapper<S> extends FunctionMetadata
    implements FunctionReplyOnlyInteractor<S> {
  private final ReplyOnlyInteractor<S> wrappedInteractor;

  public FunctionReplyOnlyInteractorWrapper(BoundedContextId boundedContextId,
      ReplyOnlyInteractor<S> wrappedInteractor) {
    super(boundedContextId);
    this.wrappedInteractor = wrappedInteractor;
  }

  @Override
  public Promise<S> invoke() {
    return wrappedInteractor.invoke();
  }
}
