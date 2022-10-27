package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFactory {

    public static Product product(final String name, final int price) {
        return new Product(name, new BigDecimal(price));
    }

    public static Product product(final String name, final BigDecimal price) {
        return new Product(name, price);
    }
}
