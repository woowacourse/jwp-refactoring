package kitchenpos.product.domain;

import java.math.BigDecimal;

public class Price {

    private BigDecimal value;

    public Price(final BigDecimal value) {
        validatePrice(value.intValue());
        this.value = value;
    }

    private void validatePrice(final Integer price) {
        if (price == null || price.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void validateMenuPrice(final BigDecimal menuPrice, final BigDecimal sumOfMenuProductsPrice){
        if (menuPrice.compareTo(sumOfMenuProductsPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
