package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.request.ProductCreateRequest;
import kitchenpos.product.ui.response.ProductResponse;

import java.math.BigDecimal;

import static io.restassured.http.ContentType.JSON;

public class ProductStep {

    public static final ProductCreateRequest PRODUCT_CREATE_REQUEST_스키야키 = new ProductCreateRequest(
            "스키야키",
            BigDecimal.valueOf(11_900)
    );

    public static final ProductCreateRequest PRODUCT_CREATE_REQUEST_우동 = new ProductCreateRequest(
            "우동",
            BigDecimal.valueOf(8_900)
    );

    public static ProductCreateRequest toRequest(final Product product) {
        return new ProductCreateRequest(
                product.getName(),
                product.getPrice()
        );
    }

    public static ProductResponse 상품_생성_요청하고_상품_반환(final ProductCreateRequest request) {
        final ExtractableResponse<Response> response = 상품_생성_요청(request);
        return response.jsonPath().getObject("", ProductResponse.class);
    }

    public static Long 상품_생성_요청하고_아이디_반환(final ProductCreateRequest request) {
        final ExtractableResponse<Response> response = 상품_생성_요청(request);
        return response.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 상품_생성_요청(final ProductCreateRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

                .when()
                .post("/api/products")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .get("/api/products")

                .then()
                .log().all()
                .extract();
    }
}
