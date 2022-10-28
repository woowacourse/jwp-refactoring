package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal price;

    public Price(final BigDecimal price) {
        validatePriceIsValid(price);
        this.price = price;
    }

    private void validatePriceIsValid(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("상품의 가격은 null 이 아니며 0원 이상이어야 합니다. [%s]", price));
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
