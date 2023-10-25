package kitchenpos.domain.product;

import java.math.BigDecimal;

public class Price {

    private final BigDecimal value;

    public Price(long value) {
        this(BigDecimal.valueOf(value));
    }

    public Price(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 한다.");
        }
        this.value = value;
    }

    public boolean isBigger(BigDecimal price) {
        return value.compareTo(price) > 0;
    }

    public BigDecimal multiplyWithQuantity(long quantity) {
        return value.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getValue() {
        return value;
    }
}
