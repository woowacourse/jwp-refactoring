package kitchenpos.product;

import domain.Product;
import java.math.BigDecimal;

public final class ProductFactory {

    private ProductFactory() {
    }

    public static Product createProductOf(final String name, final BigDecimal price) {
        return new Product(name, price);
    }
}
