package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.product.application.dto.ProductRequestDto;
import kitchenpos.product.domain.Product;

public class ProductFixture {

    public static ProductRequestDto 상품 = 상품_생성("상품", BigDecimal.valueOf(10000));

    public static ProductRequestDto 상품_생성(final String name, final BigDecimal price) {
        return new ProductRequestDto(name, price);
    }

}
