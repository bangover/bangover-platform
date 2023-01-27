package cloud.bangover.platform.domain.store;

import cloud.bangover.CollectionWrapper;
import cloud.bangover.platform.domain.functions.search.Pagination;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class MockDataStore<D> extends MockDataContainer<D>
    implements DataStore<MockDataStore.MockContext<D>, D> {
  @Override
  public Collection<D> find(Specification<MockContext<D>> specification) {
    MockContext<D> context = createContext(specification);
    return context.getData();
  }

  @Override
  public Collection<D> find(Specification<MockContext<D>> specification, Pagination pagination) {
    MockContext<D> context = createContext(specification);
    context.applyPagination(pagination);
    return context.getData();
  }

  private MockContext<D> createContext(Specification<MockContext<D>> specification) {
    MockContext<D> context = new MockContext<>(() -> CollectionWrapper.of(getState()));
    specification.applyTo(context);
    return context;
  }

  @RequiredArgsConstructor
  public static class MockContext<D> {
    @NonNull
    private final Supplier<CollectionWrapper<D>> dataSupplier;
    @NonNull
    private Pagination pagination = new Pagination();
    @NonNull
    private Predicate<D> predicate = data -> true;

    public void applyQuery(Predicate<D> predicate) {
      this.predicate = predicate;
    }

    public void applyPagination(Pagination pagination) {
      this.pagination = pagination;
    }

    public Collection<D> getData() {
      return dataSupplier.get().filter(predicate).skip(pagination.getOffset())
          .limit(pagination.getSize().longValue()).get();
    }
  }
}
