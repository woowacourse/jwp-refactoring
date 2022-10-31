package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal price;

    public Price(final BigDecimal price) {
        validate(price);

        this.price = price;
    }

    public BigDecimal get() {
        return price;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }
}
