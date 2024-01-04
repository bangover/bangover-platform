package cloud.bangover.platform.domain.values;

import org.junit.Assert;

public abstract class JaxbJUnit4AttributeAdapterTestCase<X, Y>
    extends JaxbAttributeAdapterTestCase<X, Y> implements XmlAdapterTestRunner {

  public JaxbJUnit4AttributeAdapterTestCase(X attributeValue, Y marshalledValue) {
    super(attributeValue, marshalledValue);
  }

  @Override
  public void runForwardConversionTest() {
    Assert.assertTrue(super.execute().isForwardPassed());
  }

  @Override
  public void runBackwardConversionTest() {
    Assert.assertTrue(super.execute().isBackwardPassed());
  }
}
