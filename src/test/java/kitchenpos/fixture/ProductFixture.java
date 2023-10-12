package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    private ProductFixture() {
    }

    public static class PRODUCT {
        public static Product 후라이드_치킨() {
            return Product.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .build();
        }

        public static Product 후라이드_치킨(Long price) {
            return Product.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(price))
                    .build();
        }
    }
}
