package cloud.bangover.platform.domain.store.cases;

import cloud.bangover.dataset.DataSet;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.DataContainer;
import cloud.bangover.platform.domain.store.DeleteEntity;

public abstract class DeleteEntityTestCase<
    C extends DeleteEntityTestCase.DeleteEntityContext<I, E, D>, I, E extends Entity<I>, D>
    extends StoreActionTestCase<C, D, DeleteEntity<I, E>> {

  @Override
  protected void runTestCase(C context) {
    // When
    context.getAction().delete(getEntityId(context));

    // Then
    checkActualStoreState(context, context.getDataContainer().getDataSet());
  }

  protected abstract I getEntityId(C context);

  protected abstract void checkActualStoreState(C context, DataSet<D> actualStoreState);

  public static class DeleteEntityContext<I, E extends Entity<I>, D>
      extends StoreActionContext<D, DeleteEntity<I, E>> {
    public DeleteEntityContext(DataContainer<D> dataContainer, DeleteEntity<I, E> action) {
      super(dataContainer, action);
    }
  }
}
