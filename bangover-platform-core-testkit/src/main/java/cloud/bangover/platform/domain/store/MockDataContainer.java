package cloud.bangover.platform.domain.store;

import java.util.HashSet;
import java.util.Set;

import cloud.bangover.dataset.DataSet;

public class MockDataContainer<D> implements DataContainer<D> {
  private final Set<D> state = new HashSet<>();

  @Override
  public DataSet<D> getDataSet() {
    return DataSet.of(state);
  }

  @Override
  public void loadDataSet(DataSet<D> dataSet) {
    dataSet.forEach(state::add);
  }

  @Override
  public void clear() {
    state.clear();
  }

  protected Set<D> getState() {
    return state;
  }
}
