package kitchenpos.fixture;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixtures {

    public static final Product 후라이드치킨_16000원 = new Product("후라이드치킨", BigDecimal.valueOf(16_000));
    public static final Product 양념치킨_17000원 = new Product("양념치킨", BigDecimal.valueOf(17_000));

}
