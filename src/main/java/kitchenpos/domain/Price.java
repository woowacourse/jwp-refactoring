package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private static void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품의 가격은 null이 아니고 0보다 크거나 같아야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
