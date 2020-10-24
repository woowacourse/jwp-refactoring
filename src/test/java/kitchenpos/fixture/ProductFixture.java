package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {
    public static final Product PRODUCT_FIXTURE_1 = new Product();
    public static final Product PRODUCT_FIXTURE_2 = new Product();

    static {
        PRODUCT_FIXTURE_1.setName("1");
        PRODUCT_FIXTURE_1.setPrice(BigDecimal.TEN);
        PRODUCT_FIXTURE_2.setName("2");
        PRODUCT_FIXTURE_2.setPrice(BigDecimal.TEN);
    }
}
