package kitchenpos.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.request.CreateProductRequest;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    private ProductFixture() {
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
