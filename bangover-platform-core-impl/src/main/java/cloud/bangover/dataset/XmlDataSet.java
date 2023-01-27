package cloud.bangover.dataset;

import cloud.bangover.xml.JaxbMapping;
import java.io.InputStream;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class XmlDataSet<X, D> extends DataSet<D> {
  private JaxbMapping jaxbMapping;

  public XmlDataSet(String resourcePath, Function<X, Stream<D>> itemsProvider) {
    super();
    this.jaxbMapping = new JaxbMapping(getDataTypes());
    initializeDataSet(getClass().getClassLoader(), resourcePath, itemsProvider);
  }

  private void initializeDataSet(ClassLoader classLoader, String resourcePath,
      Function<X, Stream<D>> itemsProvider) {
    initializeDataSet(classLoader.getResourceAsStream(resourcePath), itemsProvider);
  }

  private void initializeDataSet(InputStream inputData, Function<X, Stream<D>> itemsProvider) {
    getItemsFromXmlFile(inputData, itemsProvider).forEach(super::addItem);
  }

  private Stream<D> getItemsFromXmlFile(InputStream inputData,
      Function<X, Stream<D>> itemsProvider) {
    return itemsProvider.apply(jaxbMapping.readXmlData(inputData));
  }

  protected abstract Collection<Class<?>> getDataTypes();
}
