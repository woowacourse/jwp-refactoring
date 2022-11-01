package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(final BigDecimal value) {
        this.value = value;
    }

    public static Price valueOf(final BigDecimal value) {
        return new Price(value);
    }

    public void validate() {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isBiggerThan(final BigDecimal sum) {
        return value.compareTo(sum) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
