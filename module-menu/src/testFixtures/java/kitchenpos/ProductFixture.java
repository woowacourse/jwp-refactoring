package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.common.vo.Price;
import kitchenpos.product.service.dto.ProductRequest;

public class ProductFixture {

    private static final Long COUNT = 1L;
    private static final String DEFAULT_NAME = "기본 상품";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(10000);

    public static ProductRequest create() {
        return new ProductRequest(DEFAULT_NAME + COUNT, new Price(DEFAULT_PRICE));
    }
}
