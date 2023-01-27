package cloud.bangover.dataset;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class DataSetTest {
  @Test
  public void shouldBeAllowableForForeachCycleIterating() {
    // Given
    List<Integer> referenceElements = Arrays.asList(1, 2, 3);
    List<Integer> iteratedElements = new ArrayList<>();
    DataSet<Integer> dataSet = DataSet.of(referenceElements);
    // When
    for (Integer element : dataSet) {
      iteratedElements.add(element);
    }
    // Then
    Assert.assertEquals(referenceElements, iteratedElements);
  }

  @Test
  public void shouldBeCorrectlyConvertedToStream() {
    // Given
    List<Integer> referenceElements = Arrays.asList(1, 2, 3);
    List<Integer> iteratedElements = new ArrayList<>();
    DataSet<Integer> dataSet = DataSet.of(referenceElements);
    // When
    dataSet.toStream().forEach(iteratedElements::add);
    // Then
    Assert.assertEquals(referenceElements, iteratedElements);
  }

  @Test
  public void shouldBeSuccessfullyConvertedToList() {
    // Given
    List<Integer> referenceElements = Arrays.asList(1, 2, 3);
    // When
    List<Integer> iteratedElements = DataSet.of(referenceElements).toList();
    // Then
    Assert.assertEquals(referenceElements, iteratedElements);
  }

  @Test
  public void shouldBeConvertedToAnotherDataSet() {
    // Given
    List<Integer> referenceElements = Arrays.asList(1, 2, 3);
    List<String> expectedElements = Arrays.asList("1", "2", "3");
    // When
    DataSet<String> converted = DataSet.of(referenceElements).convert(value -> value.toString());
    // Then
    Assert.assertEquals(expectedElements, converted.toList());
  }
}
