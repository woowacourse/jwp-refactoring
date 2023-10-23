package kitchenpos.domain;

import kitchenpos.domain.exception.InvalidProductPriceException;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductPrice {
    
    private final BigDecimal productPrice;
    
    public ProductPrice(final BigDecimal productPrice) {
        validate(productPrice);
        this.productPrice = productPrice;
    }
    
    private void validate(final BigDecimal productPrice) {
        if (Objects.isNull(productPrice) || productPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException("상품 가격은 0원 이상이어야 합니다");
        }
    }
    
    public ProductPrice multiply(final BigDecimal value) {
        return new ProductPrice(this.productPrice.multiply(value));
    }
    
    public BigDecimal getProductPrice() {
        return productPrice;
    }
}
