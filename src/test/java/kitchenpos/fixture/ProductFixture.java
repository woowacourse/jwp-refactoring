package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 상품(
            final String name,
            final BigDecimal price
    ) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
