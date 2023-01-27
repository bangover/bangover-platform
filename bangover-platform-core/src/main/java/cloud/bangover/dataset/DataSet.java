package cloud.bangover.dataset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class DataSet<D> implements Iterable<D> {
  @Include
  private Set<D> items = new LinkedHashSet<>();

  public static <D> DataSet<D> of(Iterable<D> data) {
    return new StaticDataSet<>(data);
  }

  @Override
  public Iterator<D> iterator() {
    return items.iterator();
  }

  public Stream<D> toStream() {
    return items.stream();
  }

  public List<D> toList() {
    return toStream().collect(Collectors.toCollection(ArrayList::new));
  }

  public <T> DataSet<T> convert(Function<D, T> converter) {
    return new StaticDataSet<T>(
        toStream().collect(Collectors.mapping(converter, Collectors.toList())));
  }

  protected DataSet<D> addItem(D item) {
    items.add(item);
    return this;
  }

  public static class StaticDataSet<D> extends DataSet<D> {
    private StaticDataSet(Iterable<D> items) {
      super();
      items.forEach(item -> addItem(item));
    }
  }

  public interface DataSetLoader<D> {
    void loadDataSet(DataSet<D> dataSet);
  }
}
