package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 상품(final Long id, final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
