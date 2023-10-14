package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 상품(String name, Long price) {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
