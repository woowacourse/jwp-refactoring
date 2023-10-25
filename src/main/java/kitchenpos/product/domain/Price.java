package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.product.domain.exception.NoPriceException;

@Embeddable
public class Price {

    private static final BigDecimal LOWER_BOUND = BigDecimal.ZERO;

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    private Price(final BigDecimal value) {
        this.value = value;
    }

    public static Price from(final BigDecimal value) {
        if (value == null || value.compareTo(LOWER_BOUND) < 0) {
            throw new NoPriceException(value);
        }
        return new Price(value);
    }

    public boolean isBiggerThan(final BigDecimal otherValue) {
        return value.compareTo(otherValue) > 0;
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
