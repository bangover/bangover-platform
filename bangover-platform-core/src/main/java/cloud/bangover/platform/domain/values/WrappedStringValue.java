package cloud.bangover.platform.domain.values;

import java.util.Optional;

/**
 * This class is the base string value wrapper type.
 * 
 * @param <W> The wrapper type name
 */
public abstract class WrappedStringValue<W extends WrappedStringValue<W>>
    extends WrappedValue<W, String> {
  private static final long serialVersionUID = -9090585610718208167L;

  /**
   * Create the value wrapper without default value. If the value is null, the empty string will be
   * extracted.
   * 
   * @param value The wrapped value
   */
  protected WrappedStringValue(String value) {
    super(value, "");
  }

  /**
   * Create the value wrapper with default value.
   * 
   * @param value        The wrapped value
   * @param defaultValue The default value
   */
  protected WrappedStringValue(String value, String defaultValue) {
    super(value, defaultValue);
  }

  /**
   * @see WrappedValue#asOptional()
   * 
   *      This class overrides method and apply the {@link String#trim()} transformation to the
   *      wrapped value.
   */
  @Override
  public Optional<String> asOptional() {
    return super.asOptional().map(String::trim);
  }
}
