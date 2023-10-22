package kitchenpos.fixture;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {
    
    public static Product PRODUCT(String name, long price) {
        return new Product(
                name,
                new Price(BigDecimal.valueOf(price))
        );
    }
}
