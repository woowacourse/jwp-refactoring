package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal price;

    public Price(final BigDecimal price) {
        this.price = price;
    }

    public static Price from(final BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    private static void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 금액이 없거나, 음수입니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
