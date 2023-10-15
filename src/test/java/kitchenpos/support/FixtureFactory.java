package kitchenpos.support;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class FixtureFactory {

    public static Product forSaveProduct(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product savedProduct(final Long id, final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
