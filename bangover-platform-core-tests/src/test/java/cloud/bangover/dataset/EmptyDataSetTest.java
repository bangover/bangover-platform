package cloud.bangover.dataset;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EmptyDataSetTest {
  @Test
  public void shouldNotEmptySetContainElements() {
    // When
    DataSet<Object> dataSet = new EmptyDataSet<>();
    // Then
    Assert.assertTrue(dataSet.toList().isEmpty());
  }
}
