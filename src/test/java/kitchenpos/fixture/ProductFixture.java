package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.menu.Product;

public class ProductFixture {

    public static Product 치킨_8000원() {
        return new Product(1L, "치킨", BigDecimal.valueOf(8000));
    }

    public static Product 피자_8000원() {
        return new Product(2L, "피자", BigDecimal.valueOf(8000));
    }
}
