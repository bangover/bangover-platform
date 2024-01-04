package cloud.bangover.platform.domain.values;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public abstract class JaxbJUnit4AttributeAdapterTest {
  @Theory
  public void shouldForwardConversionBePassed(
      @FromDataPoints("TEST_CASES") XmlAdapterTestRunner testCase) {
    testCase.runForwardConversionTest();
  }

  public void shouldBackwardConversionBePassed(
      @FromDataPoints("TEST_CASES") XmlAdapterTestRunner testCase) {
    testCase.runBackwardConversionTest();
  }
}
