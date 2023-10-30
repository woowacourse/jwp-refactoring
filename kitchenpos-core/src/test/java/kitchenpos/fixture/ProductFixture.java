package kitchenpos.fixture;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;

import java.math.BigDecimal;

public class ProductFixture {
    
    public static Product PRODUCT(String name, long price) {
        return new Product(
                name,
                new ProductPrice(BigDecimal.valueOf(price))
        );
    }
}