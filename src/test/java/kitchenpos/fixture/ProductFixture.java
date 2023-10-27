package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

    public static Product 상품(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price);
    }
}
