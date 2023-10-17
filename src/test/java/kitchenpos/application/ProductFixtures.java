package kitchenpos.application;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductFixtures {

    public static Product 로제떡볶이() {
        return new Product(1L, "로제떡볶이", Price.of(1000));
    }

    public static Product 짜장떡볶이() {
        return new Product(2L, "짜장떡볶이", Price.of(100));
    }

    public static Product 마라떡볶이() {
        return new Product(3L, "마라떡볶이", Price.of(1100));
    }
}
