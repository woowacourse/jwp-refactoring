package kitchenpos.support.fixture.domain;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product getProduct(final String name, final Long price) {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
