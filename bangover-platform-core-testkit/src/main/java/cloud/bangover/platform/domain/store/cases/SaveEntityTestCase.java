package cloud.bangover.platform.domain.store.cases;

import cloud.bangover.dataset.DataSet;
import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.DataContainer;
import cloud.bangover.platform.domain.store.SaveEntity;

public abstract class SaveEntityTestCase<C extends SaveEntityTestCase.SaveEntityContext<I, E, D>, I,
    E extends Entity<I>, D> extends StoreActionTestCase<C, D, SaveEntity<I, E>> {

  @Override
  protected void runTestCase(C context) {
    // When
    context.getAction().save(getEntity(context));

    // Then
    checkActualStoreState(context, context.getDataContainer().getDataSet());
  }

  protected abstract E getEntity(C context);

  protected abstract void checkActualStoreState(C context, DataSet<D> actualStoreState);

  public static class SaveEntityContext<I, E extends Entity<I>, D>
      extends StoreActionContext<D, SaveEntity<I, E>> {
    public SaveEntityContext(DataContainer<D> dataContainer, SaveEntity<I, E> action) {
      super(dataContainer, action);
    }
  }
}
