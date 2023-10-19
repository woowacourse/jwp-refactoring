package kitchenpos.test.fixtures;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public enum ProductFixtures {
    EMPTY("empty", new BigDecimal("0")),
    BASIC("basic", new BigDecimal("16000"));

    private final String name;
    private final BigDecimal price;

    ProductFixtures(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product get() {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
