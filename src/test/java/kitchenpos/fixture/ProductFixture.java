package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product create(String name, int price) {
        Product product = new Product();
        product.setId(null);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }

}
