package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.product.exception.InvalidProductPriceException;

@Embeddable
public class ProductPrice {

    private BigDecimal price;

    public ProductPrice() {
    }

    public ProductPrice(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException();
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal multiply(Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
