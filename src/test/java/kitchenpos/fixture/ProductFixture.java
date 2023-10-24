package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static final Product PRODUCT_1 = new Product("후라이드", new BigDecimal("16000.00"));
    public static final Product PRODUCT_2 = new Product("양념치킨", new BigDecimal("16000.00"));
    public static final Product PRODUCT_3 = new Product("반반치킨", new BigDecimal("16000.00"));
    public static final Product PRODUCT_4 = new Product("통구이", new BigDecimal("16000.00"));
    public static final Product PRODUCT_5 = new Product("간장치킨", new BigDecimal("17000.00"));
    public static final Product PRODUCT_6 = new Product("순살치킨", new BigDecimal("17000.00"));

    private ProductFixture() {
    }
}
