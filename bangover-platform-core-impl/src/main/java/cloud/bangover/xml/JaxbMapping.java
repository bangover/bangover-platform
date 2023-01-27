package cloud.bangover.xml;

import cloud.bangover.errors.UnexpectedErrorException;
import lombok.NonNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * This class is the component which simplifies working with JAXB XML parsing.
 *
 * @author Dmitry Mikhaylenko
 */
public class JaxbMapping {
  private final JAXBContext jaxbContext;

  /**
   * Create {@link JaxbMapping} from data types collection.
   *
   * @param dataTypes The {@link Collection} of data types
   */
  public JaxbMapping(Collection<Class<?>> dataTypes) {
    this((Class<?>[]) dataTypes.toArray());
  }

  /**
   * Create {@link JaxbMapping} from data types array.
   *
   * @param dataTypes The array of data types
   */
  public JaxbMapping(Class<?>... dataTypes) {
    super();
    this.jaxbContext = createJaxbContext(dataTypes);
  }

  /**
   * Read XML data from resource loaded by default {@link ClassLoader}.
   *
   * @param resource The resource path
   * @param <X>      The mapping data-object type
   * @return The data-object instance with mapped data
   */
  public <X> X readXmlDataFromResource(String resource) {
    return readXmlDataFromResource(getClass().getClassLoader(), resource);
  }

  /**
   * Read XML data from resources loaded by customized {@link ClassLoader}.
   *
   * @param classLoader The {@link ClassLoader}
   * @param resource    The resource path
   * @param <X>         The mapping data-object type
   * @return The data-object instance with mapped data
   */
  public <X> X readXmlDataFromResource(ClassLoader classLoader, String resource) {
    return readXmlData(classLoader.getResourceAsStream(resource));
  }

  @SuppressWarnings("unchecked")
  public <X> X readXmlData(@NonNull InputStream xmlData) {
    try (InputStream autocloseableXmlData = xmlData) {
      return (X) createJaxbUnmarshaller().unmarshal(autocloseableXmlData);
    } catch (IOException | JAXBException error) {
      throw new UnexpectedErrorException(error);
    }
  }

  private JAXBContext createJaxbContext(Class<?>... dataTypes) {
    try {
      return JAXBContext.newInstance(dataTypes);
    } catch (JAXBException error) {
      throw new UnexpectedErrorException(error);
    }
  }

  private Unmarshaller createJaxbUnmarshaller() throws JAXBException {
    return jaxbContext.createUnmarshaller();
  }
}
