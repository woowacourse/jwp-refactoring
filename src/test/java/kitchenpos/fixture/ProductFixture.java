package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product newProduct(final Long id, final String name, final BigDecimal price) {
        return new Product(
                id,
                name,
                price
        );
    }

    public static Product newProduct(final String name, final int price) {
        return new Product(
                name,
                BigDecimal.valueOf(price)
        );
    }
}
