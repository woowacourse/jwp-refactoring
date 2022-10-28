package kitchenpos.domain;

import java.math.BigDecimal;

public class Price {

    private final BigDecimal amount;

    public Price(BigDecimal amount) {
        validateAmount(amount);

        this.amount = amount;
    }

    private void validateAmount(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
