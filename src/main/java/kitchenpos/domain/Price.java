package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private BigDecimal value;

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException();
        }
        if (BigDecimal.ZERO.compareTo(value) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal value() {
        return value;
    }
}
