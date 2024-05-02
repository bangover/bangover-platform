package cloud.bangover.platform.domain.store;

import cloud.bangover.platform.domain.functions.search.query.Pagination;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MockAskContext<V> extends MockDataContainer<V> {
  @NonNull
  private final Supplier<Collection<V>> dataSupplier;
  @NonNull
  private Pagination pagination = new Pagination();
  @NonNull
  private Predicate<V> predicate = data -> true;
  private Comparator<V> sorter = (left, right) -> 0;

  public MockAskContext() {
    super();
    this.dataSupplier = () -> getDataSet().toList();
  }

  public void applyQuery(Predicate<V> predicate) {
    this.predicate = predicate;
  }

  public void applyPagination(Pagination pagination) {
    this.pagination = pagination;
  }

  public void applySorter(Comparator<V> sorter) {
    this.sorter = sorter;
  }

  public Collection<V> getData() {
    return dataSupplier.get().stream().filter(predicate).sorted(sorter).skip(pagination.getOffset())
        .limit(pagination.getSize().longValue()).collect(Collectors.toList());
  }

}
