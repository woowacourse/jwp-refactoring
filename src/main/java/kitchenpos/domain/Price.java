package kitchenpos.domain;

import java.math.BigDecimal;

public class Price {

    private final BigDecimal value;

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 공백이거나 0원보다 작을 수 없습니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
