package cloud.bangover;

import cloud.bangover.platform.domain.values.JaxbJUnit4AttributeAdapterTest;
import cloud.bangover.platform.domain.values.JaxbJUnit4AttributeAdapterTestCase;
import cloud.bangover.platform.domain.values.XmlAdapterTestRunner;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.junit.experimental.theories.DataPoints;

public class JaxbBoundedContextIdAdapterTest extends JaxbJUnit4AttributeAdapterTest {

  @DataPoints("TEST_CASES")
  public static XmlAdapterTestRunner[] testCases() {
    return new XmlAdapterTestRunner[] { new TestCase(null, null),
        new TestCase(BoundedContextId.PLATFORM_CONTEXT,
            BoundedContextId.PLATFORM_CONTEXT.toString()),
        new TestCase(BoundedContextId.createFor("SOME_CONTEXT"), "SOME_CONTEXT") };
  }

  public static class TestCase extends JaxbJUnit4AttributeAdapterTestCase<BoundedContextId, String> {
    public TestCase(BoundedContextId attributeValue, String marshalledValue) {
      super(attributeValue, marshalledValue);
    }

    @Override
    protected XmlAdapter<String, BoundedContextId> createAttributeAdapter() {
      return new JaxbBoundedContextIdAdapter();
    }
  }
}
