package kitchenpos.support;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Product;

public enum ProductFixtures {

    PRODUCT1(1L, "후라이드", 16000),
    PRODUCT2(2L, "양념치킨", 16000),
    PRODUCT3(3L, "반반치킨", 16000),
    PRODUCT4(4L, "통구이", 16000),
    PRODUCT5(5L, "간장치킨", 17000),
    PRODUCT6(6L, "순살치킨", 17000),
    ;

    private Long id;
    private String name;
    private int price;

    ProductFixtures(final Long id, final String name, final int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product create() {
        return new Product(id, name, BigDecimal.valueOf(price));
    }

    public Product createWithNullId() {
        return new Product(null, name, BigDecimal.valueOf(price));
    }

    public static List<Product> createAll() {
        return Arrays.asList(PRODUCT1.create(), PRODUCT2.create(), PRODUCT3.create(), PRODUCT4.create(),
                PRODUCT5.create(), PRODUCT6.create());
    }
}
