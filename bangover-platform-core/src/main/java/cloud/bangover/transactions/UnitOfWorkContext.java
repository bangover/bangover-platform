package cloud.bangover.transactions;

public interface UnitOfWorkContext {
  void completeWork();

  void abortWork();
}