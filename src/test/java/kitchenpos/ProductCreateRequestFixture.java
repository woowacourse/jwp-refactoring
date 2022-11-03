package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.product.application.ProductCreateRequest;

public class ProductCreateRequestFixture {

    private static final String DEFAULT_NAME = "후라이드";

    private ProductCreateRequestFixture() {
    }

    public static ProductCreateRequest createRequest(final String name, final BigDecimal price) {
        return new ProductCreateRequest(name, price);
    }

    public static ProductCreateRequest createRequest(final BigDecimal price) {
        return new ProductCreateRequest(DEFAULT_NAME, price);
    }
}
