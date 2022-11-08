package kitchenpos.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;

    @Column(name = "price")
    private BigDecimal value;

    public Price(final BigDecimal value) {
        validateValue(value);
        this.value = value;
    }

    public static Price from(final int value) {
        return new Price(new BigDecimal(value));
    }

    public static Price sum(List<Price> prices) {
        final BigDecimal sum = prices.stream()
                .map(price -> price.value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Price(sum);
    }

    public Price multiply(final long multiplicand) {
        final BigDecimal multipliedValue = value.multiply(BigDecimal.valueOf(multiplicand));
        return new Price(multipliedValue);
    }

    public boolean isOver(final Price other) {
        return value.compareTo(other.value) > 0;
    }

    private static void validateValue(final BigDecimal value) {
        checkNull(value);
        checkMinimum(value);
    }

    private static void checkNull(final BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException();
        }
    }

    private static void checkMinimum(final BigDecimal value) {
        if (value.compareTo(MIN_VALUE) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
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

    protected Price() {
    }
}
