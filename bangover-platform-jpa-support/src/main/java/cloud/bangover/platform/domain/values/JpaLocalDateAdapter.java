package cloud.bangover.platform.domain.values;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JpaLocalDateAdapter implements AttributeConverter<LocalDate, Date> {
  @Override
  public Date convertToDatabaseColumn(LocalDate attribute) {
    return Optional.ofNullable(attribute).map(Date::valueOf).orElse(null);
  }

  @Override
  public LocalDate convertToEntityAttribute(Date dbData) {
    return Optional.ofNullable(dbData).map(Date::toLocalDate).orElse(null);
  }
}
