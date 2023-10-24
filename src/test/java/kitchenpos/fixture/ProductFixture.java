package kitchenpos.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.request.CreateProductRequest;
import kitchenpos.dto.response.CreateProductResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

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

    public static Long 상품_생성(CreateProductRequest request) {
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(201)
                .extract();
        return Long.parseLong(response.header("Location").split("/")[3]);
    }
}
