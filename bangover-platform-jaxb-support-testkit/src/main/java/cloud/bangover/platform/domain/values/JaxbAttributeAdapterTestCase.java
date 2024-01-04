package cloud.bangover.platform.domain.values;

import java.util.Objects;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public abstract class JaxbAttributeAdapterTestCase<X, Y> {
  private final X attributeValue;
  private final Y marshalledValue;

  public Report execute() {
    return new Report();
  }

  protected abstract XmlAdapter<Y, X> createAttributeAdapter();

  public class Report {
    @SneakyThrows
    public boolean isForwardPassed() {
      return Objects.equals(marshalledValue, createAttributeAdapter().marshal(attributeValue));
    }

    @SneakyThrows
    public boolean isBackwardPassed() {
      return Objects.equals(attributeValue, createAttributeAdapter().unmarshal(marshalledValue));
    }
  }
}
