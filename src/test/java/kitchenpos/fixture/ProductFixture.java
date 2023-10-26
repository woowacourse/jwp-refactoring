package kitchenpos.fixture;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductPrice;

import java.math.BigDecimal;

public class ProductFixture {
    
    public static Product PRODUCT(String name, long price) {
        return new Product(
                name,
                new ProductPrice(BigDecimal.valueOf(price))
        );
    }
}
