package kitchenpos.fixtures;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtures {

    private static final String PRODUCT_NAME = "기본 상품명";
    private static final int PRICE = 10000;

    public static Product createProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName(PRODUCT_NAME);
        product.setPrice(BigDecimal.valueOf(PRICE *100, 2));
        return product;
    }

}
