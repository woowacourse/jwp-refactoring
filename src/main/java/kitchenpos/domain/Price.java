package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal amount;

    public Price(final BigDecimal amount) {
        validateValidAmount(amount);
        this.amount = amount;
    }

    private void validateValidAmount(final BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
