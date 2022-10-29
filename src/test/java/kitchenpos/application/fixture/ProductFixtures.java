package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtures {

    public static final Product generateProduct(final String name) {
        return generateProduct(null, name, BigDecimal.valueOf(16000));
    }

    public static final Product generateProduct(final String name, final BigDecimal price) {
        return generateProduct(null, name, price);
    }

    public static final Product generateProduct(final Long id, final Product product) {
        return generateProduct(id, product.getName(), product.getPrice());
    }

    public static final Product generateProduct(final Long id, final String name, final BigDecimal price) {
        return new Product(name, price);
    }
}
