package cloud.bangover.platform.domain.store.cases;

import cloud.bangover.platform.domain.functions.search.query.Pagination;
import cloud.bangover.platform.domain.store.AskSpecification;
import java.util.Collection;

public abstract class PaginatedAskTestCase<Ctx extends AskTestCase.AskContext<C, D, V>, C, D, V>
    extends AskTestCase<Ctx, C, D, V> {
  @Override
  protected final Collection<V> ask(Ctx context, AskSpecification<C, V> askAction) {
    return askAction.ask(createSpecificationContext(context), getPagination(context)).get();
  }

  protected abstract C createSpecificationContext(Ctx context);

  protected abstract Pagination getPagination(Ctx context);
}
