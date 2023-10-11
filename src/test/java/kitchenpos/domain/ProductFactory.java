package kitchenpos.domain;

import java.math.BigDecimal;

public final class ProductFactory {

    private ProductFactory() {
    }

    public static Product createProductOf(final Long id, final String name, final BigDecimal price) {
        final Product product = createProductOf(name, price);
        product.setId(id);
        return product;
    }

    public static Product createProductOf(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
