package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {
    public static Product product() {
        return new Product(0L, "name", BigDecimal.ZERO);
    }
}
