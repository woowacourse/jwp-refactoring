package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    public static final Price ZERO = new Price("0");

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        this.value = value;
    }

    public Price(final String value) {
        this.value = new BigDecimal(value);
    }

    public Price multiply(final Quantity quantity) {
        return new Price(value.multiply(new BigDecimal(quantity.getValue())));
    }

    public boolean isGreaterThan(final Price otherPrice) {
        return value.compareTo(otherPrice.value) == 1;
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
