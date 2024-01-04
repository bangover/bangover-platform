package cloud.bangover.platform.domain.values;

import cloud.bangover.validation.context.Validatable;
import cloud.bangover.validation.context.ValidationContext;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import lombok.EqualsAndHashCode;

/**
 * This class implements the base wrapper-type over the primitives using for avoiding the primitive
 * types obsession anti-pattern. For example, you want to represent Email in your project. You may
 * use {@link String} type, but there are some questions to this approach: Which object is going to
 * be responsible for value validation? Which object is going to be responsible for operations over
 * the primitive type (for example domain name extraction)? Even if it is very simple operations it
 * may lead to duplicates in the code. Wrapper type concept is aimed to minimize potential risk.
 * Also it implements the null-object pattern.
 *
 * @param <W> The wrapper type name (usually it is the self type name)
 * @param <V> The wrapped value type name
 *
 * @author Dmitry Mikhaylenko
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class WrappedValue<W extends WrappedValue<W, V>, V>
    implements Validatable, Serializable {
  private static final long serialVersionUID = -1697887318980523859L;

  private Optional<V> value;
  private V defaultValue;

  /**
   * Create the value object via primitive without default value.
   *
   * @param value The wrapped value
   */
  protected WrappedValue(V value) {
    this(value, null);
  }

  /**
   * Create the value object via primitive with default value.
   *
   * @param value        The wrapped value
   * @param defaultValue The default value by which the object will be initialized if value is null
   */
  protected WrappedValue(V value, V defaultValue) {
    super();
    this.value = Optional.ofNullable(value);
    this.defaultValue = defaultValue;
  }

  /**
   * Get the value if it is not null or default value.
   *
   * @return The extracted value
   */
  @EqualsAndHashCode.Include
  public V getValue() {
    return asOptional().orElse(defaultValue);
  }

  /**
   * Check that the wrapped value is not null independently if the default value is specified or
   * not.
   *
   * @return True if it is present or false otherwise
   */
  public boolean isPresent() {
    return asOptional().isPresent();
  }

  /**
   * Check that object is initialized by null independently if the default value is specified or
   * not.
   * 
   * @return True if it is absent or false otherwise
   */
  public boolean isAbsent() {
    return !isPresent();
  }

  /**
   * Check that the object value is equal to the default value.
   *
   * @return True if is default and false otherwise
   */
  public boolean isDefault() {
    return Objects.equals(getValue(), defaultValue);
  }

  /**
   * Extract the value as optional type.
   *
   * @return The value wrapped by {@link Optional}
   */
  public Optional<V> asOptional() {
    return this.value;
  }

  @Override
  public ValidationContext validate(ValidationContext context) {
    return context;
  }

  @Override
  public final String toString() {
    return String.format("%s[value=%s, default=%s]", this.getClass().getName(), this.getValue(),
        this.isDefault());
  }
}
