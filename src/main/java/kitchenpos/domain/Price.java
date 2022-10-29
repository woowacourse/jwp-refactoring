package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(final BigDecimal value) {
        validatePriceNotNegative(value);
        this.value = value;
    }

    private void validatePriceNotNegative(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 양수여야 합니다.");
        }
    }

    public int compareTo(final BigDecimal other) {
        return value.compareTo(other);
    }

    public BigDecimal getValue() {
        return value;
    }
}
