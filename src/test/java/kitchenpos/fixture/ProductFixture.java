package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 스키야키() {
        return new Product(
                "스키야키",
                BigDecimal.valueOf(11_900)
        );
    }

    public static Product 우동() {
        return new Product(
                "우동",
                BigDecimal.valueOf(8_900)
        );
    }
}
