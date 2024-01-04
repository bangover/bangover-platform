package cloud.bangover.platform.domain.values;

import javax.persistence.AttributeConverter;
import org.junit.Assert;

public abstract class JpaJUnit4AttributeAdapterTestCase<X, Y>
    extends JpaAttributeAdapterTestCase<X, Y> implements JpaAttributeAdapterTestRunner {

  public JpaJUnit4AttributeAdapterTestCase(X attributeValue, Y databaseValue) {
    super(attributeValue, databaseValue);
  }

  @Override
  public void runForwardConversionTest() {
    Assert.assertTrue(super.execute().isForwardPassed());
  }

  @Override
  public void runBackwardConversionTest() {
    Assert.assertTrue(super.execute().isBackwardPassed());
  }

  protected abstract AttributeConverter<X, Y> createAttributeConverter();
}
