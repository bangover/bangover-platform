package cloud.bangover.transactions;

import cloud.bangover.errors.UnexpectedErrorException;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionDecorator;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

public class JeeUnitOfWorkFunction<Q, S> extends UnitOfWorkFunction<Q, S> {
  private final UserTransaction userTransaction;

  private JeeUnitOfWorkFunction(BusinessFunction<Q, S> original, UserTransaction userTransaction) {
    super(original);
    this.userTransaction = userTransaction;
  }

  public static BusinessFunctionDecorator decorator(UserTransaction userTransaction) {
    return new BusinessFunctionDecorator() {
      @Override
      public <Q, S> BusinessFunction<Q, S> decorate(BusinessFunction<Q, S> original) {
        return new JeeUnitOfWorkFunction<>(original, userTransaction);
      }
    };
  }

  @Override
  protected UnitOfWorkContext startWork() {
    try {
      userTransaction.begin();
      return new JeeUnitOfWorkContext(userTransaction);
    } catch (NotSupportedException | SystemException error) {
      throw new UnexpectedErrorException(error);
    }
  }

  @RequiredArgsConstructor
  private static class JeeUnitOfWorkContext implements UnitOfWorkContext {
    private final UserTransaction transaction;

    @Override
    @SneakyThrows
    public void completeWork() {
      transaction.commit();
    }

    @Override
    @SneakyThrows
    public void abortWork() {
      transaction.rollback();
    }
  }
}
