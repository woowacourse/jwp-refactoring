package kitchenpos.domain.product;

import java.math.BigDecimal;

public class ProductPrice {

    private final BigDecimal value;

    public ProductPrice(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
