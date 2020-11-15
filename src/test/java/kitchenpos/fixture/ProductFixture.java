package kitchenpos.fixture;

import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductCreateRequest;

import java.math.BigDecimal;

public class ProductFixture {
    public static ProductCreateRequest createProductRequest(String name, BigDecimal price) {
        return new ProductCreateRequest(name, price);
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }
}
