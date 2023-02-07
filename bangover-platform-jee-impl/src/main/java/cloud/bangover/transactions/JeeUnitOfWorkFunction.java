package cloud.bangover.transactions;

import cloud.bangover.errors.UnexpectedErrorException;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.functions.BusinessFunctionDecorator;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

public class JeeUnitOfWorkFunction<Q, S> extends UnitOfWorkFunction<Q, S> {
  private final TransactionManager transactionManager;

  private JeeUnitOfWorkFunction(BusinessFunction<Q, S> original,
      TransactionManager transactionManager) {
    super(original);
    this.transactionManager = transactionManager;
  }

  public static BusinessFunctionDecorator decorator(TransactionManager transactionManager) {
    return new BusinessFunctionDecorator() {
      @Override
      public <Q, S> BusinessFunction<Q, S> decorate(BusinessFunction<Q, S> original) {
        return new JeeUnitOfWorkFunction<>(original, transactionManager);
      }
    };
  }

  @Override
  protected UnitOfWorkContext startWork() {
    try {
      transactionManager.begin();
      return new JeeUnitOfWorkContext(transactionManager.getTransaction());
    } catch (NotSupportedException | SystemException error) {
      throw new UnexpectedErrorException(error);
    }
  }

  @RequiredArgsConstructor
  private static class JeeUnitOfWorkContext implements UnitOfWorkContext {
    private final Transaction transaction;

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
