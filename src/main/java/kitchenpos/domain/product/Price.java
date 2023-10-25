package kitchenpos.domain.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.relational.core.mapping.Column;

public class Price {

    public static final int MAX_PRICE_SCALE = 2;
    public static final int MAX_PRICE_PRECISION = 19;

    @JsonProperty("price")
    @Column("PRICE")
    private final BigDecimal value;

    public Price(final BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private static void validatePrice(final BigDecimal value) {
        if (Objects.isNull(value) || isNegativeValue(value)) {
            throw new IllegalArgumentException();
        }

        if (hasInvalidLengthRange(value)) {
            throw new IllegalArgumentException();
        }
    }

    private static boolean hasInvalidLengthRange(final BigDecimal value) {
        return value.scale() > MAX_PRICE_SCALE || value.precision() > MAX_PRICE_PRECISION;
    }

    private static boolean isNegativeValue(final BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public Price multiply(final Price price) {
        return new Price(this.value.multiply(price.value));
    }

    public Price add(final Price price) {
        return new Price(this.value.add(price.value));
    }

    public boolean isBiggerThan(final Price price) {
        return this.value.compareTo(price.value) > 0;
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
