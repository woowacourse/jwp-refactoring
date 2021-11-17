package kitchenpos;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static final String PRODUCT_NAME1 = "육회초밥";
    public static final String PRODUCT_NAME2 = "연어초밥";
    public static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(15900);

    public static Product createProduct1() {
        return createProduct1(PRODUCT_NAME1, PRODUCT_PRICE);
    }

    public static Product createProduct2() {
        return createProduct1(PRODUCT_NAME2, PRODUCT_PRICE);
    }

    public static Product createProduct1(Long id) {
        return createProduct1(id, PRODUCT_NAME1, PRODUCT_PRICE);
    }

    public static Product createProduct1(String name, BigDecimal price) {
        return createProduct1(null, name, price);
    }

    public static Product createProduct1(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }
}
