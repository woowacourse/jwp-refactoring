package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 후추_치킨_10000원() {
        return new Product("후추_칰힌", BigDecimal.valueOf(10000));
    }

}
