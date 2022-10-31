package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Product;

public class ProductFixtures {

    private static final BigDecimal PRICE = BigDecimal.valueOf(1000);
    private static final String NAME = "상품";

    private ProductFixtures() {
    }

    public static Product createProduct() {
        return new Product(1L, NAME, PRICE);
    }

    public static ProductResponse createProductResponse() {
        return new ProductResponse(1L, NAME, PRICE);
    }

    public static ProductCreateRequest createProductRequest() {
        return new ProductCreateRequest(NAME, PRICE);
    }
}
