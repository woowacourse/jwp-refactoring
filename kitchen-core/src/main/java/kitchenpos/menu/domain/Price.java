package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    public static final Price ZERO = new Price(BigDecimal.ZERO);
    private final BigDecimal value;

    public Price(final BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
