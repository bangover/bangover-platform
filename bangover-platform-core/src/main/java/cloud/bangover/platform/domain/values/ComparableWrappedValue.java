package cloud.bangover.platform.domain.values;

public interface ComparableWrappedValue<V extends Comparable<V>,
    W extends ComparableWrappedValue<V, W>> extends Comparable<W> {

  @Override
  default int compareTo(W opposite) {
    return getValue().compareTo(opposite.getValue());
  }

  V getValue();
}
