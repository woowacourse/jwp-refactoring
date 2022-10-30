package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0보다 커야합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isMoreExpensive(BigDecimal target) {
        return value.compareTo(target) > 0;
    }
}
