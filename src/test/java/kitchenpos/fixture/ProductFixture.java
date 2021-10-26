package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {
    public static final Product 후라이드치킨;
    public static final Product 양념치킨;
    public static final Product 반반치킨;
    public static final Product 통구이;
    public static final Product 간장치킨;
    public static final Product 순살치킨;
    public static final Product 강정치킨;

    static {
        후라이드치킨 = newInstance(1L, "후라이드치킨", new BigDecimal(16000));
        양념치킨 = newInstance(2L, "양념치킨", new BigDecimal(16000));
        반반치킨 = newInstance(3L, "반반치킨", new BigDecimal(16000));
        통구이 = newInstance(4L, "통구이", new BigDecimal(16000));
        간장치킨 = newInstance(5L, "간장치킨", new BigDecimal(17000));
        순살치킨 = newInstance(6L, "순살치킨", new BigDecimal(17000));
        강정치킨 = newInstance(7L, "강정치킨", new BigDecimal(17000));
    }

    private static Product newInstance(Long id, String name, BigDecimal price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
