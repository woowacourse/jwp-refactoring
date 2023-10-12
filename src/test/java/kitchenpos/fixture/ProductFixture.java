package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {
    public static final Product 후라이드_16000 = new Product("후라이드", BigDecimal.valueOf(16000L));
    public static final Product 양념치킨_16000 = new Product("양념치킨", BigDecimal.valueOf(16000L));
    public static final Product 순살치킨_16000 = new Product("순살치없킨", BigDecimal.valueOf(16000L));

    public static Product 상품(String name, Long price) {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
