package kitchenpos.domain.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.badrequest.NegativePriceException;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(final BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePriceException();
        }
    }

    public Price add(final Price price) {
        return new Price(value.add(price.getValue()));
    }

    public Price multiply(final long times) {
        return new Price(value.multiply(BigDecimal.valueOf(times)));
    }

    public boolean isExpensiveThan(final Price price) {
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
        if (!(o instanceof Price)) {
            return false;
        }
        Price price = (Price) o;
        return value.compareTo(price.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
