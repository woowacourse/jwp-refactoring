package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private BigDecimal price;

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;
    }

    public BigDecimal multiply(long quantity) {
        BigDecimal toBigDecimal = BigDecimal.valueOf(quantity);
        return price.multiply(toBigDecimal);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
