package cloud.bangover.xml;

import cloud.bangover.CollectionWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JaxbMappingTest {
  private static final String JAXB_XML_MAPPING_TEST_XML = "cloud/bangover/xml/JaxbMappingTest.xml";

  private JaxbMapping jaxbMapping;

  @Before
  public void setUp() {
    this.jaxbMapping = new JaxbMapping(XmlData.class, XmlDataItem.class);
  }

  @Test
  public void shouldGetXmlDataFromResource() {
    XmlData xmlData = this.jaxbMapping.readXmlDataFromResource(JAXB_XML_MAPPING_TEST_XML);
    Map<Long, String> data = xmlData.getData();
    Assert.assertEquals("First", data.get(1L));
    Assert.assertEquals("Second", data.get(2L));
    Assert.assertEquals("Third", data.get(3L));
  }

  @XmlRootElement(name = "data")
  public static class XmlData {
    @XmlElement(name = "item")
    @XmlElementWrapper(name = "items")
    private List<XmlDataItem> data = new ArrayList<>();

    public Map<Long, String> getData() {
      return CollectionWrapper.of(data).asMap(xmlData -> xmlData.getId(),
          xmlData -> xmlData.getValue());
    }
  }

  @XmlRootElement
  public static class XmlDataItem {
    @Getter
    @XmlAttribute
    private Long id;
    @Getter
    @XmlValue
    private String value;
  }
}
