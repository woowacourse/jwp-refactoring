package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.domain.exception.NoPriceException;

public class Price {

    private static final BigDecimal LOWER_BOUND = BigDecimal.ZERO;

    private final BigDecimal value;

    private Price(final BigDecimal value) {
        this.value = value;
    }

    public static Price from(final BigDecimal value) {
        if (value == null || value.compareTo(LOWER_BOUND) < 0) {
            throw new NoPriceException(value);
        }
        return new Price(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
