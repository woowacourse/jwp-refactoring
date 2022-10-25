package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createProduct(final int price) {
        return new Product("간장 치킨", BigDecimal.valueOf(price));
    }
}
