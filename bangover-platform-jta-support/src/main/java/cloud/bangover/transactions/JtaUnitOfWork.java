package cloud.bangover.transactions;

import cloud.bangover.errors.UnexpectedErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
      UnitOfWorkContext context = new JeeUnitOfWorkContext(userTransaction, startTransaction());
      notifyStarted();
      return context;
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

  @RequiredArgsConstructor
  private class JeeUnitOfWorkContext implements UnitOfWorkContext {
    private final UserTransaction transaction;
    @Getter(value = AccessLevel.PRIVATE)
    private final boolean started;

    @Override
    @SneakyThrows
    public void completeWork() {
      if (isStarted()) {
        transaction.commit();
        notifyCompleted();
      }
    }

    @Override
    @SneakyThrows
    public void abortWork() {
      if (isStarted()) {
        transaction.rollback();
        notifyAborted();
      }
    }
  }
}
