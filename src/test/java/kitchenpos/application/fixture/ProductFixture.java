package kitchenpos.application.fixture;

import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Product;

import java.math.BigDecimal;

public abstract class ProductFixture {

    private ProductFixture() {
    }

    public static Product product(final String name, final BigDecimal price) {
        final Price menuPrice = new Price(price);
        return new Product(name, menuPrice);
    }

    public static Product noodle() {
        return product("noodle", BigDecimal.valueOf(6000));
    }

    public static Product potato() {
        return product("potato", BigDecimal.valueOf(3000));
    }
}
