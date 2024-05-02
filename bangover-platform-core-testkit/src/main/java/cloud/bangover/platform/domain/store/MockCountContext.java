package cloud.bangover.platform.domain.store;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MockCountContext<V> extends MockDataContainer<V> {
  @NonNull
  private final Supplier<Collection<V>> dataSupplier;
  @NonNull
  private Predicate<V> predicate = data -> true;

  public MockCountContext() {
    super();
    this.dataSupplier = () -> getDataSet().toList();
  }

  public void applyQuery(Predicate<V> predicate) {
    this.predicate = predicate;
  }

  public Long getCount() {
    return dataSupplier.get().stream().filter(predicate).count();
  }
}
