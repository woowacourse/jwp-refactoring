package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    private static final String PRODUCT_NAME = "후라이드 치킨";

    public static Product createProduct(Long id, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(PRODUCT_NAME);
        product.setPrice(price);
        return product;
    }

    public static Product createProductWithId(Long id) {
        return createProduct(id, null);
    }

    public static Product createProductWithPrice(BigDecimal price) {
        return createProduct(null, price);
    }
}
