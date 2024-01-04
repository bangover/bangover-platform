package cloud.bangover.platform.domain.values;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class JaxbWrappedValueAdapter<W extends WrappedValue<W, V>, V>
    extends XmlAdapter<V, W> {

  @Override
  public W unmarshal(V value) throws Exception {
    return wrapValue(value);
  }

  @Override
  public V marshal(W wrapped) throws Exception {
    return wrapped.getValue();
  }

  protected abstract W wrapValue(V original);
}
