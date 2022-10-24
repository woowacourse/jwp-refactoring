package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createDefaultWithoutId() {
        final Product product = new Product();
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(10000L));
        return product;
    }

    public static Product createWithPrice(final Long price) {
        final Product product = createDefaultWithoutId();
        if (price == null) {
            product.setPrice(null);
            return product;
        }
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
