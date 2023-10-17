package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_망고_1000원() {
        final var product = new Product();
        product.setId(1L);
        product.setName("망고");
        product.setPrice(BigDecimal.valueOf(1000));
        return product;
    }

    public static Product 상품_치킨_15000원() {
        final var product = new Product();
        product.setId(2L);
        product.setName("치킨");
        product.setPrice(BigDecimal.valueOf(15000));
        return product;
    }
}
