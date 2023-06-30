package cloud.bangover.platform.domain.store.cases;

import cloud.bangover.platform.domain.Entity;
import cloud.bangover.platform.domain.store.DataContainer;
import cloud.bangover.platform.domain.store.RetrieveEntity;
import java.util.Optional;

public abstract class RetrieveEntityTestCase<
    C extends RetrieveEntityTestCase.RetrieveEntityContext<I, E, D>, I, E extends Entity<I>, D>
    extends StoreActionTestCase<C, D, RetrieveEntity<I, E>> {

  @Override
  protected final void runTestCase(C context) {
    // When
    Optional<E> retrievedEntity = context.getAction().find(getEntityId(context));

    // Then
    checkActualRetrievedEntityState(context, retrievedEntity);
  }

  protected abstract I getEntityId(C context);

  protected abstract void checkActualRetrievedEntityState(C context, Optional<E> actualRetrievedEntity);

  public static class RetrieveEntityContext<I, E extends Entity<I>, D>
      extends StoreActionContext<D, RetrieveEntity<I, E>> {
    public RetrieveEntityContext(DataContainer<D> dataContainer, RetrieveEntity<I, E> action) {
      super(dataContainer, action);
    }
  }
}
