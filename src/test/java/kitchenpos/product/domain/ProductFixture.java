package kitchenpos.product.domain;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 상품(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price);
    }
}
