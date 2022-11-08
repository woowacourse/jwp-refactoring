package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.dto.ProductRequest;

public class ProductFixture {

    public static ProductRequest createProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
