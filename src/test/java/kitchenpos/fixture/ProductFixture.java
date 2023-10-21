package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_망고_1000원() {
        return new Product("망고", BigDecimal.valueOf(1000));
    }

    public static Product 상품_망고_N원(final int price) {
        return new Product("망고", BigDecimal.valueOf(price));
    }

    public static Product 상품_치킨_15000원() {
        return new Product("치킨", BigDecimal.valueOf(15000));
    }
}
