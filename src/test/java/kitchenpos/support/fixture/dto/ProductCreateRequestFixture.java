package kitchenpos.support.fixture.dto;

import java.math.BigDecimal;
import kitchenpos.application.dto.product.ProductCreateRequest;

public abstract class ProductCreateRequestFixture {

    public static ProductCreateRequest productCreateRequest(final String name, final Long price) {
        return new ProductCreateRequest(name, BigDecimal.valueOf(price));
    }
}
