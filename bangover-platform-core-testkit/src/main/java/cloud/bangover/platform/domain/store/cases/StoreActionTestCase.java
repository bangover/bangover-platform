package cloud.bangover.platform.domain.store.cases;

import cloud.bangover.dataset.DataSet;

public abstract class StoreActionTestCase<C extends StoreActionContext<D, A>, D, A> {

  public void execute(C context) {
    try {
      setUp(context);
      runTestCase(context);
    } finally {
      tearDown(context);
    }
  }

  public void setUp(C context) {
    context.getDataContainer().loadDataSet(getInitialStoreState(context));
  }

  public void tearDown(C context) {
    context.getDataContainer().clear();
  }

  protected abstract DataSet<D> getInitialStoreState(C context);

  protected abstract void runTestCase(C context);
}
