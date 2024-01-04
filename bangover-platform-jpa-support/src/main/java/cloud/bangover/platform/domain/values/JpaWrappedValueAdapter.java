package cloud.bangover.platform.domain.values;

import javax.persistence.AttributeConverter;

public abstract class JpaWrappedValueAdapter<W extends WrappedValue<W, V>, V>
    implements AttributeConverter<W, V> {

  @Override
  public final V convertToDatabaseColumn(W attribute) {
    return attribute.getValue();
  }

  @Override
  public final W convertToEntityAttribute(V dbData) {
    return wrapValue(dbData);
  }

  protected abstract W wrapValue(V value);
}
