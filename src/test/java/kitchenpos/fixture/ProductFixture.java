package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.function.Function;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 상품_저장(
            final Function<Product, Product> persistable,
            final BigDecimal price
    ) {
        final Product product = new Product("상품", price);
        return persistable.apply(product);
    }

    public static Product 상품_저장(final Function<Product, Product> productPersistable) {
        final Product product = new Product("허니콤보", new BigDecimal("20000"));
        return productPersistable.apply(product);
    }
}
