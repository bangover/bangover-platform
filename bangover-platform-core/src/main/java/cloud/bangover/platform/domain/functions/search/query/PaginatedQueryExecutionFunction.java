package cloud.bangover.platform.domain.functions.search.query;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.store.AskSpecification;
import cloud.bangover.platform.domain.store.AskSpecification.AskParameterizedSpecFactory;

public class PaginatedQueryExecutionFunction<C, Q extends PaginatedQuery, V>
    implements BusinessFunction<Q, CollectionWrapper<V>> {
  private final C context;
  private final AskParameterizedSpecFactory<C, Q, V> specificationFactory;

  public PaginatedQueryExecutionFunction(C context,
      AskParameterizedSpecFactory<C, Q, V> specificationFactory) {
    super();
    this.context = context;
    this.specificationFactory = specificationFactory;
  }

  @Override
  public void invoke(Context<Q, CollectionWrapper<V>> context) {
    Q query = context.getRequest();
    Pagination pagination = query.getPagination();
    AskSpecification<C, V> specification = specificationFactory.createSpecification(query);
    context.reply(specification.ask(this.context, pagination));
  }
}
