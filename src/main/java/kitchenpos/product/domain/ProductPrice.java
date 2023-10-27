package kitchenpos.product.domain;

import javax.persistence.Column;
import java.math.BigDecimal;

public class ProductPrice {

    @Column(name = "price")
    private BigDecimal value;

    protected ProductPrice() {
    }

    private ProductPrice(final BigDecimal value) {
        validateMoreThanZero(value);
        this.value = value;
    }

    private void validateMoreThanZero(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static ProductPrice of(final BigDecimal value) {
        return new ProductPrice(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
