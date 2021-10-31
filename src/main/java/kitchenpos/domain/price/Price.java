package kitchenpos.domain.price;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.quantity.Quantity;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public class Price {

    private static final String PRICE_NEGATIVE_ERROR_MESSAGE = "Price는 0보다 작을 수 없습니다.";

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public Price(Integer value) {
        validate(value);
        this.value = BigDecimal.valueOf(value);
    }

    private void validate(Object object) {
        validateNonNull(object);
        validateNotNegative(object);
    }

    private void validateNotNegative(Object object) {
        if (object instanceof Integer) {
            validateIntegerNotNegative((Integer) object);
            return;
        }
        if (object instanceof BigDecimal) {
            validateBigDecimalNotNegative((BigDecimal) object);
            return;
        }
        throw new IllegalArgumentException("유효하지 않은 타입입니다.");
    }

    private void validateIntegerNotNegative(Integer value) {
        if (value < 0) {
            throw new InvalidArgumentException(PRICE_NEGATIVE_ERROR_MESSAGE);
        }
    }

    private void validateBigDecimalNotNegative(BigDecimal value) {
        if (value.intValue() < 0) {
            throw new InvalidArgumentException(PRICE_NEGATIVE_ERROR_MESSAGE);
        }
    }

    private void validateNonNull(Object value) {
        if (Objects.isNull(value)) {
            throw new InvalidArgumentException("Price는 null일 수 없습니다.");
        }
    }

    public int getValueAsInt() {
        return value.intValue();
    }

    public Price multiply(Quantity quantity) {
        final BigDecimal quantityValue = BigDecimal.valueOf(quantity.getValue());
        final BigDecimal result = value.multiply(quantityValue);
        return new Price(result);
    }

    public boolean isGreaterOrEqualThan(Price otherPrice) {
        return value.compareTo(otherPrice.value) >= 0;
    }

    public Integer getValueAsInteger() {
        return value.intValue();
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
