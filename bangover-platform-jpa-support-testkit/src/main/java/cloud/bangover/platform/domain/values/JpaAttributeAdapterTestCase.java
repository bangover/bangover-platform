package cloud.bangover.platform.domain.values;

import java.util.Objects;
import javax.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class JpaAttributeAdapterTestCase<X, Y> {
  private final X attributeValue;
  private final Y databaseValue;

  public Report execute() {
    return new Report();
  }

  protected abstract AttributeConverter<X, Y> createAttributeConverter();

  public class Report {
    public boolean isForwardPassed() {
      return Objects.equals(databaseValue,
          createAttributeConverter().convertToDatabaseColumn(attributeValue));
    }

    public boolean isBackwardPassed() {

      return Objects.equals(attributeValue,
          createAttributeConverter().convertToEntityAttribute(databaseValue));
    }
  }
}
