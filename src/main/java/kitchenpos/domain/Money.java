package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private static final int MINIMUM_VALUE = 0;

    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money valueOf(BigDecimal value) {
        validatePrice(value);
        return new Money(value);
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MINIMUM_VALUE) {
            throw new IllegalArgumentException("가격은 " + MINIMUM_VALUE + " 미만일 수 없습니다.");
        }
    }

    public boolean isGreaterThan(BigDecimal price) {
        return value.compareTo(price) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
