package kitchenpos.product.domain;

import kitchenpos.product.exception.InvalidProductPriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {
    
    private BigDecimal price;
    
    public ProductPrice() {
    }
    
    public ProductPrice(final BigDecimal price) {
        validate(price);
        this.price = price;
    }
    
    private void validate(final BigDecimal productPrice) {
        if (Objects.isNull(productPrice) || productPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException("상품 가격은 0원 이상이어야 합니다");
        }
    }
    
    public ProductPrice multiply(final BigDecimal value) {
        return new ProductPrice(this.price.multiply(value));
    }
    
    public BigDecimal getPrice() {
        return price;
    }
}
