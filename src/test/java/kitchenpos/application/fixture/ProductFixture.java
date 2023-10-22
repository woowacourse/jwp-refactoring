package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createProduct(final String name, final Long price) {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public static Product createProduct(final Long id, final String name, final Long price) {
        return new Product(id, name, BigDecimal.valueOf(price));
    }
}
