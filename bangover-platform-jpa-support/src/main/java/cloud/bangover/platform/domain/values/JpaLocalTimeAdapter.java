package cloud.bangover.platform.domain.values;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JpaLocalTimeAdapter implements AttributeConverter<LocalTime, Time> {
  @Override
  public Time convertToDatabaseColumn(LocalTime attribute) {
    return Optional.ofNullable(attribute).map(Time::valueOf).orElse(null);
  }

  @Override
  public LocalTime convertToEntityAttribute(Time dbData) {
    return Optional.ofNullable(dbData).map(Time::toLocalTime).orElse(null);
  }
}
