package cloud.bangover.transactions;

import cloud.bangover.errors.UnexpectedErrorException;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class JtaUnitOfWork implements UnitOfWork {
  private final UserTransaction userTransaction;

  @Override
  public UnitOfWorkContext startWork() {
    try {
      return new JeeUnitOfWorkContext(userTransaction, startTransaction());
    } catch (NotSupportedException | SystemException error) {
      throw new UnexpectedErrorException(error);
    }
  }

  private boolean startTransaction() throws NotSupportedException, SystemException {
    if (isNotStarted()) {
      userTransaction.begin();
      return true;
    }
    return false;
  }

  private boolean isNotStarted() throws SystemException {
    return userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION;
  }

  @RequiredArgsConstructor
  private static class JeeUnitOfWorkContext implements UnitOfWorkContext {
    private final UserTransaction transaction;
    @Getter(value = AccessLevel.PRIVATE)
    private final boolean started;

    @Override
    @SneakyThrows
    public void completeWork() {
      if (isStarted()) {
        transaction.commit();
      }
    }

    @Override
    @SneakyThrows
    public void abortWork() {
      if (isStarted()) {
        transaction.rollback();
      }
    }
  }
}
