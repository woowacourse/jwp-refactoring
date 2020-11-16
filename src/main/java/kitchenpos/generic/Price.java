package kitchenpos.generic;

import java.math.BigDecimal;

public class Price {
    private final BigDecimal price;

    public Price(BigDecimal price) {
        this.price = price;
    }

    public boolean isLessThan(Long price) {
        return this.price.compareTo(BigDecimal.valueOf(price)) < 0;
    }

    public boolean isLessThan(BigDecimal price) {
        return this.price.compareTo(price) < 0;
    }

    public Long longValue() {
        return this.price.longValue();
    }
}
