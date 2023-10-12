package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtures {

    public static final Product 후라이드치킨_16000원 = create("후라이드치킨", BigDecimal.valueOf(16_000));
    public static final Product 후라이드치킨_16000원_ID1 = create(1L,"후라이드치킨", BigDecimal.valueOf(16_000));
    public static final Product 양념치킨_17000원 = create("양념치킨", BigDecimal.valueOf(17_000));

    private static Product create(String name, BigDecimal price) {
        return create(null, name, price);
    }

    private static Product create(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
