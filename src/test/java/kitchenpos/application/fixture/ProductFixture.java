package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 후라이드_치킨() {
        return new Product("후라이드치킨", BigDecimal.valueOf(18000));
    }

    public static Product 양념_치킨() {
        return new Product("양념치킨", BigDecimal.valueOf(19000));
    }
}
