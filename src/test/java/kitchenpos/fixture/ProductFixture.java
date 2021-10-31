package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {
    private static final Product product = new Product(0L, "name", BigDecimal.ZERO);

    public static Product product() {
        return product;
    }
}
