package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    private ProductFixture() {
    }

    public static class PRODUCT {
        public static Product 후라이드_치킨() {
            Product product = new Product();
            product.setId(1L);
            product.setName("후라이드치킨");
            product.setPrice(BigDecimal.valueOf(16000L));
            return product;
        }
    }
}
