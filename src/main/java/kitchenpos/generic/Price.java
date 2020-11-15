package kitchenpos.generic;

import java.math.BigDecimal;

public class Price {
    private final BigDecimal price;

    public Price(BigDecimal price) {
        this.price = price;
    }

    public Price(Long price) {
        this(BigDecimal.valueOf(price));
    }

    public static Price ofZero() {
        return new Price(0L);
    }

    public Price add(BigDecimal price) {
        return new Price(this.price.add(price));
    }

    public boolean isLessThan(BigDecimal price) {
        return this.price.compareTo(price) < 0;
    }

    public Long longValue() {
        return this.price.longValue();
    }
}
