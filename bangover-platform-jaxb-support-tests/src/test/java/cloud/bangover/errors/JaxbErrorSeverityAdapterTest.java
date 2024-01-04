package cloud.bangover.errors;

import cloud.bangover.errors.ErrorDescriptor.ErrorSeverity;
import cloud.bangover.platform.domain.values.JaxbJUnit4AttributeAdapterTest;
import cloud.bangover.platform.domain.values.JaxbJUnit4AttributeAdapterTestCase;
import cloud.bangover.platform.domain.values.XmlAdapterTestRunner;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;

public class JaxbErrorSeverityAdapterTest extends JaxbJUnit4AttributeAdapterTest {
  private static JaxbErrorSeverityAdapter ADAPTER = new JaxbErrorSeverityAdapter();

  @DataPoints("TEST_CASES")
  public static XmlAdapterTestRunner[] testCases() {
    return new XmlAdapterTestRunner[] { new TestCase(null, null),
        new TestCase(ErrorSeverity.BUSINESS, "BUSINESS") };
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldMarshallingBeCompletedWithErrorIfValueIsUnknown() throws Exception {
    ADAPTER.unmarshal("UNKNOWN_VALUE");
  }

  public static class TestCase extends JaxbJUnit4AttributeAdapterTestCase<ErrorSeverity, String> {
    public TestCase(ErrorSeverity attributeValue, String marshalledValue) {
      super(attributeValue, marshalledValue);
    }

    @Override
    protected XmlAdapter<String, ErrorSeverity> createAttributeAdapter() {
      return ADAPTER;
    }
  }
}
