package cloud.bangover.platform.domain.functions.search.query;

import cloud.bangover.platform.domain.store.DataStore;
import cloud.bangover.platform.domain.store.Specification;

public class PaginatedQueryExecutionFunction<C, Q extends PaginatedQuery, V> extends QueryExecutionFunction<C, Q, V> {
    public PaginatedQueryExecutionFunction(
            DataStore<C, V> entityStore,
            Specification.ParameterizedFactory<C, Q> specificationFactory
    ) {
        super(entityStore, specificationFactory);
    }
}
