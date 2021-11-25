package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

public class ProductFixture {
    public static Product product() {
        return new Product(0L, "name", BigDecimal.ZERO);
    }

    public static ProductRequest productRequest() {
        return new ProductRequest("name", BigDecimal.ZERO);
    }
}
