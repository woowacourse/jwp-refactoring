package kitchenpos.domain.fixture;

import kitchenpos.common.Price;
import kitchenpos.product.Product;

import java.math.BigDecimal;

public abstract class ProductFixture {

    private ProductFixture() {
    }

    public static Product product(final String name, final BigDecimal price) {
        final Price menuPrice = new Price(price);
        return new Product(name, menuPrice);
    }
}
