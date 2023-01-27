package cloud.bangover.transactions;

import cloud.bangover.transactions.UnitOfWorkFunction.UnitOfWorkContext;

public class MockUnitOfWorkContext implements UnitOfWorkContext {
  private UOWState state = UOWState.PENDING;

  public boolean isCompleted() {
    return state == UOWState.COMPLETED;
  }

  public boolean isAborted() {
    return state == UOWState.ABORTED;
  }

  public void reset() {
    state = UOWState.PENDING;
  }

  @Override
  public void completeWork() {
    state = UOWState.COMPLETED;
  }

  @Override
  public void abortWork() {
    state = UOWState.ABORTED;
  }

  public enum UOWState {
    PENDING,
    COMPLETED,
    ABORTED;
  }
}
