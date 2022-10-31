package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.exception.PriceException;

public class Price {

    private final BigDecimal value;

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
