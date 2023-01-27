package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.Entity;

import java.util.Optional;
import java.util.function.Function;

public interface EntityStore<I, E extends Entity<I>> {
  Optional<E> find(I id);

  void save(E entity);

  void delete(I id);
  
  default <C extends Entity<I>> EntityStore<I, C> derive(
      Function<C, E> forwardEntityConverter,
      Function<E, C> backwardEntityConverter) {
    return derive(id -> id, forwardEntityConverter, backwardEntityConverter);
  }
  
  default <A, C extends Entity<A>> EntityStore<A, C> derive(
      Function<A, I> idConverter,
      Function<C, E> forwardEntityConverter,
      Function<E, C> backwardEntityConverter) {
    EntityStore<I, E> store = this;
    return new EntityStore<A, C>() {
      @Override
      public Optional<C> find(A id) {
        return store
            .find(idConverter.apply(id))
            .map(backwardEntityConverter);
      }

      @Override
      public void save(C entity) {
        store.save(forwardEntityConverter.apply(entity));
      }

      @Override
      public void delete(A id) {
        store.delete(idConverter.apply(id));
      }
    };
  }
}
