package cloud.bangover.platform.domain.store.cases;

import cloud.bangover.dataset.DataSet;
import cloud.bangover.platform.domain.store.AskSpecification;
import cloud.bangover.platform.domain.store.DataContainer;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(value = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class AskTestCase<Ctx extends AskTestCase.AskContext<C, D, V>, C, D, V>
    extends StoreActionTestCase<Ctx, D, AskSpecification<C, V>> {
  @Override
  protected final void runTestCase(Ctx context) {
    // When
    DataSet<V> result = DataSet.of(ask(context, context.getAction()));

    // Then
    checkActualQueryResult(context, result);
  }

  protected abstract Collection<V> ask(Ctx context, AskSpecification<C, V> askAction);

  protected abstract void checkActualQueryResult(Ctx context, DataSet<V> actualQueryResult);

  public static class AskContext<C, D, V> extends StoreActionContext<D, AskSpecification<C, V>> {
    public AskContext(DataContainer<D> dataContainer, AskSpecification<C, V> action) {
      super(dataContainer, action);
    }
  }
}
