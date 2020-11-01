package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private BigDecimal value;

    private Price() {
    }

    public Price(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public Price(Double value) {
        this(BigDecimal.valueOf(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isBiggerThen(BigDecimal price) {
        return value.compareTo(price) > 0;
    }
}
