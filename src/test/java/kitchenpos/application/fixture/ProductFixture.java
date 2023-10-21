package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createProduct(final String name, final Long price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }

    public static Product createProduct(final Long id, final String name, final Long price) {
        final Product product = createProduct(name, price);
        product.setId(id);

        return product;
    }
}
