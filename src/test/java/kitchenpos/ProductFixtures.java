package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Product;

public class ProductFixtures {

    public static Product createProduct() {
        return new Product(1L, "상품A", BigDecimal.valueOf(600));
    }

    public static ProductResponse createProductResponse() {
        return new ProductResponse(1L, "상품", BigDecimal.valueOf(1000));
    }

    public static ProductCreateRequest createProductRequest() {
        return new ProductCreateRequest("상품", BigDecimal.valueOf(1000));
    }
}
