package kitchenpos;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static final String PRODUCT_NAME = "육회초밥";
    public static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(15900);

    public static Product createProduct() {
        return createProduct(PRODUCT_NAME, PRODUCT_PRICE);
    }

    public static Product createProduct(Long id) {
        return createProduct(id, PRODUCT_NAME, PRODUCT_PRICE);
    }

    public static Product createProduct(String name, BigDecimal price) {
        return createProduct(null, name, price);
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }
}
