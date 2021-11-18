package kitchenpos;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static final String PRODUCT_NAME1 = "육회초밥";
    public static final String PRODUCT_NAME2 = "연어초밥";
    public static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(15900);

    public static Product createProduct1() {
        return createProduct(PRODUCT_NAME1, PRODUCT_PRICE);
    }

    public static Product createProduct2() {
        return createProduct(PRODUCT_NAME2, PRODUCT_PRICE);
    }

    public static Product createProduct(String name, BigDecimal price) {
        return new Product(name, price);
    }
}
