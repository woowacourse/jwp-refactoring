package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFactory {

    public static Product product(final String name, final int price) {
        final var product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));

        return product;
    }

    public static Product product(final String name, final BigDecimal price) {
        final var product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
