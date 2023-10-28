package kitchenpos.fixture;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product PRODUCT_1 = Product.of("후라이드", new BigDecimal("16000.00"));
    public static Product PRODUCT_2 = Product.of("양념치킨", new BigDecimal("16000.00"));
    public static Product PRODUCT_3 = Product.of("반반치킨", new BigDecimal("16000.00"));
    public static Product PRODUCT_4 = Product.of("통구이", new BigDecimal("16000.00"));
    public static Product PRODUCT_5 = Product.of("간장치킨", new BigDecimal("17000.00"));
    public static Product PRODUCT_6 = Product.of("순살치킨", new BigDecimal("17000.00"));

    private ProductFixture() {
    }
}
