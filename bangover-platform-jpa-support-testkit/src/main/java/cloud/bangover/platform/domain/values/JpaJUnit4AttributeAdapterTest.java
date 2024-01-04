package cloud.bangover.platform.domain.values;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public abstract class JpaJUnit4AttributeAdapterTest {
  @Theory
  public void shouldForwardConversionBePassed(
      @FromDataPoints("TEST_CASES") JpaAttributeAdapterTestRunner testCase) {
    testCase.runForwardConversionTest();
  }

  @Theory
  public void shouldBackwardConversionBePassed(
      @FromDataPoints("TEST_CASES") JpaAttributeAdapterTestRunner testCase) {
    testCase.runBackwardConversionTest();
  }
}
