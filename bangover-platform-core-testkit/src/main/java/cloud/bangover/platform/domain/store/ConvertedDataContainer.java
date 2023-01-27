package cloud.bangover.platform.domain.store;

import cloud.bangover.dataset.DataSet;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ConvertedDataContainer<D, T> implements DataContainer<T> {
  private final Function<D, T> forwardConverter;
  private final Function<T, D> backwardConverter;
  private final DataContainer<D> originalContainer;

  @Override
  public DataSet<T> getDataSet() {
    return originalContainer.getDataSet().convert(forwardConverter);
  }

  @Override
  public void loadDataSet(DataSet<T> dataSet) {
    originalContainer.loadDataSet(dataSet.convert(backwardConverter));
  }

  @Override
  public void clear() {
    originalContainer.clear();
  }
}
