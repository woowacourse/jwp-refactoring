package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 상품(String name, Long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
