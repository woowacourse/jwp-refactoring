package kitchenpos.fixtures;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product PRODUCT() {
        final Product product = new Product();
        product.setName("라면맛 요구르트");
        product.setPrice(BigDecimal.valueOf(1000));
        return product;
    }

    public static Product PRODUCT(final BigDecimal price) {
        final Product product = new Product();
        product.setName("라면맛 요구르트");
        product.setPrice(price);
        return product;
    }
}
