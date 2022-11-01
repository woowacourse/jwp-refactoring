package kitchenpos.fixture.dto;

import java.math.BigDecimal;
import kitchenpos.ui.dto.request.ProductCreateRequest;

public class ProductDtoFixture {

    public static ProductCreateRequest createProductCreateRequest(final String name, final BigDecimal price) {
        return new ProductCreateRequest(name, price);
    }
}
