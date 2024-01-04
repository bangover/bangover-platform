package cloud.bangover.platform.domain.values;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * This class is the base big decimal value wrapper type.
 * 
 * @param <W> The wrapper type name
 */
public abstract class WrappedBigDecimalValue<W extends WrappedBigDecimalValue<W>>
    extends WrappedValue<W, BigDecimal> implements ComparableWrappedValue<BigDecimal, W> {
  private static final long serialVersionUID = 6632872565277649313L;

  /**
   * Create without default value.
   * 
   * @param value    The value
   * @param scale    The scale value
   * @param rounding The rounding mode
   */
  protected WrappedBigDecimalValue(BigDecimal value, int scale, RoundingMode rounding) {
    this(value, null, scale, rounding);
  }

  /**
   * Create with default value.
   * 
   * @param value        The wrapped value
   * @param defaultValue The default value
   * @param scale        The scale value
   * @param rounding     The rounding mode
   */
  protected WrappedBigDecimalValue(BigDecimal value, BigDecimal defaultValue, int scale,
      RoundingMode rounding) {
    super(normalize(value, scale, rounding), normalize(defaultValue, scale, rounding));
  }

  private static BigDecimal normalize(BigDecimal value, int scale, RoundingMode rounding) {
    return Optional.ofNullable(value).map(v -> v.setScale(scale, rounding)).orElse(null);
  }
}
