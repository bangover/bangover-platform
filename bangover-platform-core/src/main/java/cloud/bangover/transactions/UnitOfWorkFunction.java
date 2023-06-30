package cloud.bangover.transactions;

import cloud.bangover.BoundedContextId;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionDecorator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UnitOfWorkFunction<Q, S> implements BusinessFunction<Q, S> {
  private final BusinessFunction<Q, S> original;
  private final UnitOfWork unitOfWork;

  public static BusinessFunctionDecorator decorator(UnitOfWork unitOfWork) {
    return new BusinessFunctionDecorator() {
      @Override
      public <Q, S> BusinessFunction<Q, S> decorate(BusinessFunction<Q, S> original) {
        return new UnitOfWorkFunction<>(original, unitOfWork);
      }
    };
  }

  @Override
  public void invoke(Context<Q, S> functionContext) {
    UnitOfWorkContext workContext = unitOfWork.startWork();
    try {
      invokeFunction(functionContext, workContext);
    } catch (Exception error) {
      abortOnError(error, functionContext, workContext);
    }
  }

  private void invokeFunction(Context<Q, S> functionContext, UnitOfWorkContext unitOfWorkContext) {
    original.invoke(new Context<Q, S>() {
      @Override
      public BoundedContextId getBoundedContextId() {
        return functionContext.getBoundedContextId();
      }

      @Override
      public Q getRequest() {
        return functionContext.getRequest();
      }

      @Override
      public void reply(S response) {
        unitOfWorkContext.completeWork();
        functionContext.reply(response);
      }

      @Override
      public void reject(Exception error) {
        abortOnError(error, functionContext, unitOfWorkContext);
      }
    });
  }

  private void abortOnError(Exception error, Context<Q, S> functionContext,
      UnitOfWorkContext unitOfWorkContext) {
    unitOfWorkContext.abortWork();
    functionContext.reject(error);
  }
}
