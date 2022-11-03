package kitchenpos.fixture.dto;

import java.math.BigDecimal;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;

public class ProductDtoFixture {

    public static ProductRequest 짜장면_요청 = new ProductRequest("짜장면", BigDecimal.valueOf(8_000));

    public static ProductResponse 짜장면_응답 = new ProductResponse(1L, "짜장면", BigDecimal.valueOf(8_000));
    public static ProductResponse 짬뽕_응답 = new ProductResponse(1L, "짜장면", BigDecimal.valueOf(8_000));
    public static ProductResponse 탕수육_응답 = new ProductResponse(1L, "짜장면", BigDecimal.valueOf(10_000));
}
