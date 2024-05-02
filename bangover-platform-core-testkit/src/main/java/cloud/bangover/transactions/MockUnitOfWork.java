package cloud.bangover.transactions;

public class MockUnitOfWork implements UnitOfWork {
  private final MockUnitOfWorkContext context = new MockUnitOfWorkContext();

  @Override
  public UnitOfWorkContext startWork() {
    context.reset();
    return context;
  }

  public boolean isCompleted() {
    return context.isCompleted();
  }

  public boolean isAborted() {
    return context.isAborted();
  }
}