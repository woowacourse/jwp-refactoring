package kitchenpos.product.domain;

import kitchenpos.common.exception.CustomException;
import kitchenpos.common.exception.ExceptionType;
import java.math.BigDecimal;
import javax.persistence.Column;

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
            throw new CustomException(ExceptionType.PRICE_RANGE, String.valueOf(price));
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
