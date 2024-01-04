package cloud.bangover.platform.domain.values;

import java.util.Optional;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class JaxbSafeAdapter<ValueType, BoundType>
    extends XmlAdapter<ValueType, BoundType> {

  @Override
  public BoundType unmarshal(ValueType value) throws Exception {
    return this.unmarshal(Optional.ofNullable(value)).orElse(null);
  }

  @Override
  public ValueType marshal(BoundType value) throws Exception {
    return this.marshal(Optional.ofNullable(value)).orElse(null);
  }

  protected abstract Optional<BoundType> unmarshal(Optional<ValueType> value) throws Exception;

  protected abstract Optional<ValueType> marshal(Optional<BoundType> value) throws Exception;
}
