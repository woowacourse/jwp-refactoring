package kitchenpos.application;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtures {

    public static Product 로제떡볶이() {
        return new Product(1L, "로제떡볶이", BigDecimal.valueOf(1000));
    }

    public static Product 짜장떡볶이() {
        return new Product(2L, "짜장떡볶이", BigDecimal.valueOf(100));
    }

    public static Product 마라떡볶이() {
        return new Product(3L, "마라떡볶이", BigDecimal.valueOf(900));
    }
}
