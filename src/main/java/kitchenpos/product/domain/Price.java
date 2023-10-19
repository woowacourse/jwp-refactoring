package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

  public static final Price ZERO = new Price(BigDecimal.ZERO);
  private final BigDecimal value;

  public Price(final BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public boolean isNull() {
    return Objects.isNull(value);
  }

  public boolean isLessThan(final Price compare) {
    return value.compareTo(compare.value) < 0;
  }

  public boolean isGreaterThan(final Price compare) {
    return value.compareTo(compare.value) > 0;
  }

  public Price multiply(final long count) {
    return new Price(value.multiply(BigDecimal.valueOf(count)));
  }

  public Price add(final Price price) {
    return new Price(value.add(price.value));
  }
}
