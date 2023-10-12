package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtures {

    public static final Product CHICKEN_PRICE_18000 = create("치킨", BigDecimal.valueOf(18_000));

    private static Product create(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
