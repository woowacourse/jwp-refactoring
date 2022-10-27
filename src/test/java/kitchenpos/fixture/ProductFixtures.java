package kitchenpos.fixture;

import kitchenpos.dto.ProductCreateRequest;

import java.math.BigDecimal;

public class ProductFixtures {

    public static ProductCreateRequest createProductRequest(final String name, final BigDecimal price) {
        return new ProductCreateRequest(name, price);
    }

    public static ProductCreateRequest 후라이드() {
        return createProductRequest("후라이드", BigDecimal.valueOf(16000));
    }

    public static ProductCreateRequest 양념치킨() {
        return createProductRequest("양념치킨", BigDecimal.valueOf(16000));
    }

    public static ProductCreateRequest 통구이() {
        return createProductRequest("통구이", BigDecimal.valueOf(16000));
    }

    public static ProductCreateRequest 간장치킨() {
        return createProductRequest("간장치킨", BigDecimal.valueOf(17000));
    }
}
