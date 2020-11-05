package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductFixture {
    public static Product createWithOutId(BigDecimal price) {
        Product product = new Product();
        product.setId(null);
        product.setName("TEST_PRODUCT");
        product.setPrice(price);

        return product;
    }
}
