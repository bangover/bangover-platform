package cloud.bangover.platform.domain.store;

import cloud.bangover.dataset.DataSet;
import java.util.function.Function;

public interface DataContainer<D> {
  default <T> DataContainer<T> convertedTo(Function<D, T> forwardConverter,
      Function<T, D> backwardConverter) {
    return new ConvertedDataContainer<>(forwardConverter, backwardConverter, this);
  }

  DataSet<D> getDataSet();

  void loadDataSet(DataSet<D> dataSet);

  void clear();

}
