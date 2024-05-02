package cloud.bangover.platform.domain.values;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JpaInstantAdapter implements AttributeConverter<Instant, Timestamp> {
  @Override
  public Timestamp convertToDatabaseColumn(Instant attribute) {
    return Optional.ofNullable(attribute).map(Timestamp::from).orElse(null);
  }

  @Override
  public Instant convertToEntityAttribute(Timestamp dbData) {
    return Optional.ofNullable(dbData).map(Timestamp::toInstant).orElse(null);
  }
}
