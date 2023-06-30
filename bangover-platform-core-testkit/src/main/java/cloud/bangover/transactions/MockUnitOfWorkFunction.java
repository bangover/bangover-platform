package cloud.bangover.transactions;

import cloud.bangover.functions.BusinessFunction;

public class MockUnitOfWorkFunction<Q, S> implements BusinessFunction<Q, S> {
  private final MockUnitOfWork unitOfWork;
  private final BusinessFunction<Q, S> wrappedFunction;

  private MockUnitOfWorkFunction(BusinessFunction<Q, S> original) {
    super();
    this.unitOfWork = new MockUnitOfWork();
    this.wrappedFunction = UnitOfWorkFunction.decorator(unitOfWork).decorate(original);
  }

  @Override
  public void invoke(Context<Q, S> context) {
    wrappedFunction.invoke(context);
  }

  public static <Q, S> MockUnitOfWorkFunction<Q, S> createFor(BusinessFunction<Q, S> original) {
    return new MockUnitOfWorkFunction<Q, S>(original);
  }

  public boolean isCompleted() {
    return unitOfWork.isCompleted();
  }

  public boolean isAborted() {
    return unitOfWork.isAborted();
  }

  private static class MockUnitOfWork implements UnitOfWork {
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
}
