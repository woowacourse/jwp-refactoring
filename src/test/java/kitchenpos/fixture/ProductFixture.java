package kitchenpos.fixture;

import java.math.BigDecimal;

import kitchenpos.application.dto.request.CreateProductRequest;
import kitchenpos.application.dto.response.CreateProductResponse;
import kitchenpos.application.dto.response.ProductResponse;
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

    public static class REQUEST {
        public static CreateProductRequest 후라이드_치킨_16000원() {
            return CreateProductRequest.builder()
                    .name("후라이드치킨")
                    .price("16000")
                    .build();
        }

        public static CreateProductRequest 후라이드_치킨_N원(String price) {
            return CreateProductRequest.builder()
                    .name("후라이드치킨")
                    .price(price)
                    .build();
        }
    }

    public static class RESPONSE {
        public static CreateProductResponse 후라이드_치킨_16000원_생성_응답() {
            return CreateProductResponse.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price("16000")
                    .build();
        }

        public static ProductResponse 후라이드_치킨_16000원_응답() {
            return ProductResponse.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price("16000")
                    .build();
        }
    }
}
