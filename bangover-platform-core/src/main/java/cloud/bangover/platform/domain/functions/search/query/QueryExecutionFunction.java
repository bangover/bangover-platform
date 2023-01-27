package cloud.bangover.platform.domain.functions.search.query;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.functions.BusinessFunction;
import cloud.bangover.platform.domain.store.DataStore;
import cloud.bangover.platform.domain.store.Specification;

public class QueryExecutionFunction<C, Q, V> implements BusinessFunction<Q, CollectionWrapper<V>> {
  private final DataStore<C, V> entityStore;
  private final Specification.ParameterizedFactory<C, Q> specificationFactory;

  public QueryExecutionFunction(DataStore<C, V> entityStore,
      Specification.Factory<C> specificationFactory) {
    this(entityStore, requestData -> specificationFactory.createSpecification());
  }

  public QueryExecutionFunction(DataStore<C, V> entityStore,
      Specification.ParameterizedFactory<C, Q> specificationFactory) {
    this.entityStore = entityStore;
    this.specificationFactory = specificationFactory;
  }

  @Override
  public void invoke(Context<Q, CollectionWrapper<V>> context) {
    Specification<C> specification = specificationFactory.createSpecification(context.getRequest());
    context.reply(CollectionWrapper.of(entityStore.find(specification)));
  }
}
