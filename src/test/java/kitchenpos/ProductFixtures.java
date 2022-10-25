package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtures {

    public static Product createProduct() {
        return new Product(1L, "상품A", BigDecimal.valueOf(600));
    }
}
