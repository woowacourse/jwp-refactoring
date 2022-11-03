package kitchenpos.vo;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    private Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public static Price valueOf(final BigDecimal value) {
        return new Price(value);
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isBiggerThan(final BigDecimal number) {
        return value.compareTo(number) > 0;
    }

    public BigDecimal multiply(final long number) {
        return value.multiply(BigDecimal.valueOf(number));
    }

    public BigDecimal getValue() {
        return value;
    }
}
