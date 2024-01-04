package cloud.bangover.platform.domain.values;

import java.util.Optional;
import javax.persistence.AttributeConverter;

public abstract class JpaSafeAdapter<X, Y> implements AttributeConverter<X, Y> {
  @Override
  public final Y convertToDatabaseColumn(X attribute) {
    return toDatabaseColumn(Optional.ofNullable(attribute)).orElse(null);
  }

  @Override
  public final X convertToEntityAttribute(Y dbData) {
    return toEntityAttribute(Optional.ofNullable(dbData)).orElse(null);
  }

  protected abstract Optional<Y> toDatabaseColumn(Optional<X> value);

  protected abstract Optional<X> toEntityAttribute(Optional<Y> value);
}
