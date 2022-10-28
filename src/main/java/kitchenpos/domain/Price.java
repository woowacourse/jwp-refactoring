package kitchenpos.domain;

import java.math.BigDecimal;

public class Price {

    private BigDecimal price;

    public Price() {
    }

    public Price(final BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
