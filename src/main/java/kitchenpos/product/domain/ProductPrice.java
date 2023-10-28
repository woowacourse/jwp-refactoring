package kitchenpos.product.domain;

import static kitchenpos.exception.ExceptionType.PRICE_RANGE;

import java.math.BigDecimal;
import javax.persistence.Column;
import kitchenpos.exception.CustomException;

public class ProductPrice {

    @Column(name = "price")
    private BigDecimal value;

    protected ProductPrice() {
    }

    public ProductPrice(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException(PRICE_RANGE, String.valueOf(price));
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
