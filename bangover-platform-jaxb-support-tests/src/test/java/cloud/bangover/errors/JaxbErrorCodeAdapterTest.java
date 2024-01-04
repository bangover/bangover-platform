package cloud.bangover.errors;

import cloud.bangover.errors.ErrorDescriptor.ErrorCode;
import cloud.bangover.platform.domain.values.JaxbJUnit4AttributeAdapterTest;
import cloud.bangover.platform.domain.values.JaxbJUnit4AttributeAdapterTestCase;
import cloud.bangover.platform.domain.values.XmlAdapterTestRunner;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.junit.experimental.theories.DataPoints;

public class JaxbErrorCodeAdapterTest extends JaxbJUnit4AttributeAdapterTest {
  @DataPoints("TEST_CASES")
  public static XmlAdapterTestRunner[] testCases() {
    return new XmlAdapterTestRunner[] { new TestCase(null, null), new TestCase(ErrorCode.createFor(1L), 1L) };
  }

  public static class TestCase extends JaxbJUnit4AttributeAdapterTestCase<ErrorCode, Long> {
    public TestCase(ErrorCode attributeValue, Long marshalledValue) {
      super(attributeValue, marshalledValue);
    }

    @Override
    protected XmlAdapter<Long, ErrorCode> createAttributeAdapter() {
      return new JaxbErrorCodeAdapter();
    }
  }
}
