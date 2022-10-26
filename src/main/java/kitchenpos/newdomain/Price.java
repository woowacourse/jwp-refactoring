package kitchenpos.newdomain;

import java.math.BigDecimal;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;

public class Price {

    private BigDecimal value;

    public Price(final BigDecimal value) {
        validatePositive(value);
        this.value = value;
    }

    public static Price valueOf(final int value) {
        return new Price(BigDecimal.valueOf(value));
    }

    private void validatePositive(final BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainLogicException(CustomErrorCode.PRICE_MIN_VALUE_ERROR);
        }
    }

    public BigDecimal getValue() {
        return this.value;
    }
}
