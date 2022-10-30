package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal price;

    public Price(final BigDecimal price) {
        validatePriceIsValid(price);
        this.price = price;
    }

    public BigDecimal multiply(final long count) {
        return price.multiply(new BigDecimal(count));
    }

    public boolean isHigherThan(final BigDecimal other) {
        return price.compareTo(other) > 0;
    }

    private void validatePriceIsValid(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("상품의 가격은 null 이 아니며 0원 이상이어야 합니다. [%s]", price));
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Price{" +
            "price=" + price +
            '}';
    }
}
