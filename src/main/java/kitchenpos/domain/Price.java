package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(final BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 양수여야합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
