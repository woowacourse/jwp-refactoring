package kitchenpos.fixtures;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtures {

    private static final String PRODUCT_NAME = "기본 상품명";
    private static final int PRICE = 10000;
    private static final long PRODUCT_ID = 1L;

    public static Product createProduct(
        Long id,
        String name,
        long price
    ) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price * 100, 2));
        return product;
    }

    public static Product createProduct() {
        return createProduct(PRODUCT_ID, PRODUCT_NAME, PRICE);
    }

    public static Product createProduct(long price) {
        return createProduct(PRODUCT_ID, PRODUCT_NAME, price);
    }

}
