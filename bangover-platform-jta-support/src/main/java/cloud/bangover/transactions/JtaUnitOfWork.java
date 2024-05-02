package cloud.bangover.transactions;

import cloud.bangover.errors.UnexpectedErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JtaUnitOfWork implements UnitOfWork, UnitOfWorkExtensionSupport {
  private final UserTransaction userTransaction;
  private final List<UnitOfWorkExtension> extensions = new ArrayList<>();

  @Override
  public void addExtension(UnitOfWorkExtension extension) {
    this.extensions.add(extension);
  }

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
      notifyStarted();
      return true;
    }
    return false;
  }

  private boolean isNotStarted() throws SystemException {
    return userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION;
  }

  private void notifyStarted() {
    notifyExtensions(UnitOfWorkExtension::onStarted);
  }

  private void notifyCompleted() {
    notifyExtensions(UnitOfWorkExtension::onCompleted);
  }

  private void notifyAborted() {
    notifyExtensions(UnitOfWorkExtension::onAborted);
  }

  private void notifyExtensions(Consumer<UnitOfWorkExtension> handler) {
    this.extensions.forEach(handler);
  }

  @AllArgsConstructor
  private class JeeUnitOfWorkContext implements UnitOfWorkContext {
    private final UserTransaction transaction;
    @Getter(value = AccessLevel.PRIVATE)
    private boolean started;

    @Override
    public void completeWork() {
      if (isStarted()) {
        commitTransaction();
      }
    }

    @Override
    public void abortWork() {
      if (isStarted()) {
        rollbackTransaction();
      }
    }

    private void commitTransaction() {
      try {
        transaction.commit();
        notifyCompleted();
      } catch (SecurityException | IllegalStateException | RollbackException
          | HeuristicMixedException | HeuristicRollbackException | SystemException error) {
        this.started = false;
        notifyAborted();
        throw new UnexpectedErrorException(error);
      }
    }

    private void rollbackTransaction() {
      try {
        this.started = false;
        transaction.rollback();
        notifyAborted();
      } catch (IllegalStateException | SecurityException | SystemException error) {
        throw new UnexpectedErrorException(error);
      }
    }
  }
}
