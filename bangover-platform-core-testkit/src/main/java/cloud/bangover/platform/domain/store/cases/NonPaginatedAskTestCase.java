package cloud.bangover.platform.domain.store.cases;

import cloud.bangover.platform.domain.store.AskSpecification;
import java.util.Collection;

public abstract class NonPaginatedAskTestCase<Ctx extends AskTestCase.AskContext<C, D, V>, C, D, V>
    extends AskTestCase<Ctx, C, D, V> {

  @Override
  protected Collection<V> ask(Ctx context, AskSpecification<C, V> askAction) {
    return askAction.ask(createSpecificationContext(context));
  }

  protected abstract C createSpecificationContext(Ctx context);
}
