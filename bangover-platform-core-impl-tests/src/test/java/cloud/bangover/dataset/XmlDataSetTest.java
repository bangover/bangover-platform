package cloud.bangover.dataset;

import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(JUnit4.class)
public class XmlDataSetTest {
  private static final String XML_DATA_SET_TEST_XML_PATH =
      "cloud/bangover/dataset/XmlDataSetTest.xml";

  @Test
  public void shouldXmlDataSetBeSuccessfullyInitialized() {
    // When
    DataSet<String> dataSet = new TestXmlDataSet(XML_DATA_SET_TEST_XML_PATH);
    // Then
    Assert.assertEquals(Arrays.asList("1", "2", "3"), dataSet.toList());
  }

  public class TestXmlDataSet extends XmlDataSet<XmlData, String> {
    public TestXmlDataSet(String resourcePath) {
      super(resourcePath, xmlData -> xmlData.getItems().stream());
    }

    @Override
    protected Collection<Class<?>> getDataTypes() {
      return Arrays.asList(XmlData.class);
    }
  }

  @Getter
  @XmlRootElement(name = "data")
  public static class XmlData {
    @XmlElement(name = "item")
    private List<String> items = new ArrayList<>();
  }
}
