package cloud.bangover.transactions;

import cloud.bangover.functions.BusinessFunction;

public class MockUnitOfWorkFunction<Q, S> extends UnitOfWorkFunction<Q, S> {
  private final MockUnitOfWorkContext context = new MockUnitOfWorkContext();

  public MockUnitOfWorkFunction(BusinessFunction<Q, S> original) {
    super(original);
  }

  public boolean isCompleted() {
    return context.isCompleted();
  }

  public boolean isAborted() {
    return context.isAborted();
  }

  @Override
  protected UnitOfWorkContext startWork() {
    context.reset();
    return context;
  }
}
