package kitchenpos.domain;

import java.math.BigDecimal;

public class Price {

    private BigDecimal value;

    public Price(final BigDecimal value) {
        validPrice(value);
        this.value = value;
    }

    private void validPrice(final BigDecimal price) {
        if (price == null || price.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
