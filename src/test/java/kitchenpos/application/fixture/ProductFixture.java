package kitchenpos.application.fixture;

import kitchenpos.domain.common.Price;
import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public abstract class ProductFixture {

    private ProductFixture() {
    }

    public static Product product(final String name, final BigDecimal price) {
        final Price menuPrice = new Price(price);
        return new Product(name, menuPrice);
    }
}
