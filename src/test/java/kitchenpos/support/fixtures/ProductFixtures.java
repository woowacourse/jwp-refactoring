package kitchenpos.support.fixtures;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public enum ProductFixtures {

    CHICKEN("치킨", new BigDecimal(10_000));

    private final String name;
    private final BigDecimal price;

    ProductFixtures(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product create() {
        return new Product(null, name, price);
    }

    public Product createWithPrice(final BigDecimal price) {
        return new Product(null, name, price);
    }
}
