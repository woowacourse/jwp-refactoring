package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(BigDecimal price) {
        validateNull(price);
        validateNegative(price);
        this.value = price;
    }

    private static void validateNull(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("price가 존재하지 않습니다.");
        }
    }

    private static void validateNegative(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("price가 음수입니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
