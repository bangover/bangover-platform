package cloud.bangover.transactions;

import cloud.bangover.functions.BusinessFunction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UnitOfWorkFunction<Q, S> implements BusinessFunction<Q, S> {
  private final BusinessFunction<Q, S> original;

  @Override
  public void invoke(Context<Q, S> functionContext) {
    UnitOfWorkContext workContext = startWork();
    try {
      invokeFunction(functionContext, workContext);
    } catch (Exception error) {
      abortOnError(error, functionContext, workContext);
    }
  }

  private void invokeFunction(Context<Q, S> functionContext, UnitOfWorkContext unitOfWorkContext) {
    original.invoke(new Context<Q, S>() {
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

  protected abstract UnitOfWorkContext startWork();
  
  private void abortOnError(Exception error, Context<Q, S> functionContext, UnitOfWorkContext unitOfWorkContext) {
    unitOfWorkContext.abortWork();
    functionContext.reject(error);
  }

  public interface UnitOfWorkContext {
    void completeWork();

    void abortWork();
  }
}
