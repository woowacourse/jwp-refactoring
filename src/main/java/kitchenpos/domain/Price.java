package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private final BigDecimal price;

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 한다.");
        }
    }

    public Price(double price) {
        this(BigDecimal.valueOf(price));
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isExpensive(BigDecimal sum) {
        return price.longValue() > sum.longValue();
    }
}
