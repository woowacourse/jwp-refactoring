package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 후추_치킨_10000원() {
        return new Product("후추_칰힌", BigDecimal.valueOf(10000));
    }

    public static Product 매튜_치킨_10000원() {
        return new Product("매튜_칰힌", BigDecimal.valueOf(10000));
    }

    public static Product 후추_칰힌_가격_책정(BigDecimal price) {
        return new Product("후추_칰힌", price);
    }

}
