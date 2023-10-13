package kitchenpos.supports;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    private static final Long COUNT = 1L;
    private static final String DEFAULT_NAME = "기본 상품";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(10000);

    public static Product create() {
        final Product product = new Product();
        product.setName(DEFAULT_NAME + COUNT);
        product.setPrice(DEFAULT_PRICE);
        return product;
    }
}
