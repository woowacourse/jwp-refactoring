package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.domain.Product.Product;

public final class ProductFactory {

    private ProductFactory() {
    }

    public static Product createProductOf(final String name, final BigDecimal price) {
        return new Product(name, price);
    }
}
