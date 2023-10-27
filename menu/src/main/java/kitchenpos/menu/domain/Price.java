package kitchenpos.menu.domain;

import static kitchenpos.menu.exception.PriceExceptionType.PRICE_CAN_NOT_NEGATIVE;
import static kitchenpos.menu.exception.PriceExceptionType.PRICE_CAN_NOT_NULL;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.menu.exception.PriceException;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new PriceException(PRICE_CAN_NOT_NULL);
        }
        if (BigDecimal.ZERO.compareTo(value) > 0) {
            throw new PriceException(PRICE_CAN_NOT_NEGATIVE);
        }
    }

    public Price multiply(long value) {
        return new Price(this.value.multiply(BigDecimal.valueOf(value)));
    }

    public Price add(Price other) {
        return new Price(value.add(other.value));
    }

    public boolean isGreaterThan(Price other) {
        return value.compareTo(other.value) > 0;
    }

    public BigDecimal value() {
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
        return value.compareTo(price.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
