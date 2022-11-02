package kitchenpos.support.fixture;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Product;

public class ProductFixture {

    public static Product createPepperoni() {
        return new Product("페퍼로니피자", BigDecimal.valueOf(1000L));
    }

    public static Product createPineapple() {
        return new Product("파인애플피자", BigDecimal.valueOf(1000L));
    }
}
