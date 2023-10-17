package kitchenpos.domain;

import java.math.BigDecimal;

public class Price {

    private final BigDecimal price;

    private Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price of(final int price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price of(final BigDecimal price) {
        return new Price(price);
    }

    private void validate(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("가격은 NULL이 될 수 없습니다.");
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
