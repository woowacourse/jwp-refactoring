package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(BigDecimal.valueOf(0));

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        this.value = value;
    }

    public Price add(final Price price) {
        return new Price(value.add(price.value));
    }

    public Price multiply(final long quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
    }

    public boolean isGreaterThan(final Price price) {
        return this.value.compareTo(price.value) > 0;
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
}
