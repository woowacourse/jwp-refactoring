package kitchenpos.application.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public abstract class ProductFixture {

    private ProductFixture() {
    }

    public static Product product(final String name, final BigDecimal price) {
        return new Product(name, price);
    }

    public static Product noodle() {
        return new Product("noodle", BigDecimal.valueOf(6000));
    }

    public static Product potato() {
        return new Product("potato", BigDecimal.valueOf(3000));
    }
}
