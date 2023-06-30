package cloud.bangover.platform.domain.functions.search.query;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.store.AskSpecification;

public class QueryExecutionFunction<C, Q, V> implements BusinessFunction<Q, CollectionWrapper<V>> {
  private final C context;
  private final AskSpecification.AskParameterizedSpecFactory<C, Q, V> specificationFactory;

  public QueryExecutionFunction(C context,
      AskSpecification.AskSpecFactory<C, V> specificationFactory) {
    this(context, requestData -> specificationFactory.createSpecification());
  }

  public QueryExecutionFunction(C context,
      AskSpecification.AskParameterizedSpecFactory<C, Q, V> specificationFactory) {
    this.context = context;
    this.specificationFactory = specificationFactory;
  }

  @Override
  public void invoke(Context<Q, CollectionWrapper<V>> context) {
    Q query = context.getRequest();
    AskSpecification<C, V> specification = specificationFactory.createSpecification(query);
    context.reply(specification.ask(this.context));
  }
}
