package kitchenpos.test.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

    public static Product 상품(String name, BigDecimal price) {
        return new Product(name, price);
    }
}
