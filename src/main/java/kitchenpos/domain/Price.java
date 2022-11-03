package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validatePriceIsNotNullAndIsHigherMinimum(value);
        this.value = value;
    }

    public BigDecimal multiply(final long count) {
        return value.multiply(BigDecimal.valueOf(count));
    }

    public boolean isHigherThan(final BigDecimal other) {
        return value.compareTo(other) > 0;
    }

    private void validatePriceIsNotNullAndIsHigherMinimum(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("가격은 null 이 아니며 0원 이상이어야 합니다. [%s]", value));
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Price{" +
            "price=" + value +
            '}';
    }
}
