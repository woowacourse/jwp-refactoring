package kitchenpos.fixture;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product product(String name, long price) {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
