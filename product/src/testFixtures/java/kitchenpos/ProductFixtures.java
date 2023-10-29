package kitchenpos;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductFixtures {

    private ProductFixtures() {
    }

    public static Product of(final String name, final int price) {
        return new Product(name, Price.valueOf(price));
    }
}
