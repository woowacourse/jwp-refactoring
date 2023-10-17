package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }
}
