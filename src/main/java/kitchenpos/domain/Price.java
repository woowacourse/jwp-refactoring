package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static Price ZERO = new Price(BigDecimal.ZERO);

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(final BigDecimal price) {
        if (BigDecimal.ZERO.compareTo(price) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public Price add(final Price other) {
        BigDecimal added = other.value.add(value);
        return new Price(added);
    }

    public Price multiply(final long number) {
        BigDecimal multiplied = value.multiply(BigDecimal.valueOf(number));
        return new Price(multiplied);
    }

    public boolean isGreaterThan(final Price other) {
        return value.compareTo(other.value) > 0;
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
        final Price price1 = (Price) o;
        return Objects.equals(value, price1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
