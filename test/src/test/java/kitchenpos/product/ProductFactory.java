package kitchenpos.product;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public final class ProductFactory {

    private ProductFactory() {
    }

    public static Product createProductOf(final String name, final BigDecimal price) {
        return new Product(name, price);
    }
}
