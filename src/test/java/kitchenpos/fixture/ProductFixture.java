package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.domain.Product;

public class ProductFixture {
    public static Product createProduct(Long id, String name, Long price) {
        return new Product(
            id,
            name,
            BigDecimal.valueOf(price)
        );
    }

    public static ProductCreateRequest createProductRequest(String name, Long price) {
        return new ProductCreateRequest(
            name,
            price != null ? BigDecimal.valueOf(price) : null
        );
    }
}
