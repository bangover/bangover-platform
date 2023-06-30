package cloud.bangover.platform.domain.store;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.platform.domain.functions.search.query.Pagination;

public interface AskSpecification<C, V> {
  default CollectionWrapper<V> ask(C context) {
    return ask(context, getPagination());
  }

  default Pagination getPagination() {
    return new Pagination();
  }

  CollectionWrapper<V> ask(C context, Pagination pagination);

  default <D> AskSpecification<C, D> deriveWithCollectionConvertation(AskedViewCollectionMapper<V, D> viewCollectionMapper) {
    AskSpecification<C, V> spec = this;
    return new AskSpecification<C, D>() {
      @Override
      public CollectionWrapper<D> ask(C context, Pagination pagination) {
        return viewCollectionMapper.convert(spec.ask(context, pagination));
      }
    };
  }

  default <D> AskSpecification<C, D> deriveWithItemsConvertation(AskedViewMapper<V, D> viewMapper) {
    AskSpecification<C, V> spec = this;
    return new AskSpecification<C, D>() {
      @Override
      public CollectionWrapper<D> ask(C context, Pagination pagination) {
        return spec.ask(context, pagination).map(viewMapper::convert);
      }
    };
  }

  interface AskSpecFactory<C, V> {
    AskSpecification<C, V> createSpecification();
  }

  interface AskParameterizedSpecFactory<C, Q, V> {
    AskSpecification<C, V> createSpecification(Q query);
  }

  interface AskedViewMapper<V, D> {
    D convert(V view);
  }

  interface AskedViewCollectionMapper<V, D> {
    CollectionWrapper<D> convert(CollectionWrapper<V> views);
  }
}
