package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validatePositive(value);
        this.value = value;
    }

    public static Price valueOf(final int value) {
        return new Price(BigDecimal.valueOf(value));
    }

    private void validatePositive(final BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainLogicException(CustomErrorCode.PRICE_MIN_VALUE_ERROR);
        }
    }

    public Price multiply(final Quantity quantity) {
        return new Price(this.value.multiply(BigDecimal.valueOf(quantity.getValue())));
    }

    public Price sum(final Price price) {
        return new Price(this.value.add(price.value));
    }

    public boolean isGreaterThan(final Price price) {
        return this.value.compareTo(price.getValue()) > 0;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
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
