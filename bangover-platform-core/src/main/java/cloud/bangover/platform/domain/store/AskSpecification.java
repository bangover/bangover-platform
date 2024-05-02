package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.functions.search.query.Pagination;
import java.util.Collection;
import java.util.stream.Collectors;

public interface AskSpecification<C, V> {
  default Collection<V> ask(C context) {
    return ask(context, getPagination());
  }

  default Pagination getPagination() {
    return new Pagination();
  }

  Collection<V> ask(C context, Pagination pagination);

  default <D> AskSpecification<C, D> deriveWithCollectionConvertation(
      AskedViewCollectionMapper<V, D> viewCollectionMapper) {
    AskSpecification<C, V> spec = this;
    return new AskSpecification<C, D>() {
      @Override
      public Collection<D> ask(C context, Pagination pagination) {
        return viewCollectionMapper.convert(spec.ask(context, pagination));
      }
    };
  }

  default <D> AskSpecification<C, D> deriveWithItemsConvertation(AskedViewMapper<V, D> viewMapper) {
    AskSpecification<C, V> spec = this;
    return new AskSpecification<C, D>() {
      @Override
      public Collection<D> ask(C context, Pagination pagination) {
        return spec.ask(context, pagination).stream().map(viewMapper::convert)
            .collect(Collectors.toList());
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
    Collection<D> convert(Collection<V> views);
  }
}
