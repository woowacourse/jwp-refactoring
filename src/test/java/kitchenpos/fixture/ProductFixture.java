package kitchenpos.fixture;

import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product product(String name, long price) {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public static ProductRequest productRequest(String name, long price) {
        return new ProductRequest(name, new BigDecimal(price));
    }
}
