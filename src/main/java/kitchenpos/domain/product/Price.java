package kitchenpos.domain.product;

import kitchenpos.exception.KitchenposException;

import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.exception.ExceptionInformation.PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE;

public class Price {
    public static final BigDecimal MIN_PRICE = new BigDecimal(0);
    public static final BigDecimal MAX_PRICE = new BigDecimal(Integer.MAX_VALUE);

    private final BigDecimal value;

    public Price(final BigDecimal value) {
        this.value = value;
    }

    public static Price from(final BigDecimal value) {
        validateBound(value);
        return new Price(value);
    }

    private static void validateBound(final BigDecimal value) {
        if (value.compareTo(MIN_PRICE) < 0 || value.compareTo(MAX_PRICE) > 0) {
            throw new KitchenposException(PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE);
        }
    }

    public BigDecimal multiply(BigDecimal other) {
        return this.value.multiply(other);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
