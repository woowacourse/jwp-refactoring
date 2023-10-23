package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.ProductRequest;

import java.math.BigDecimal;

import static io.restassured.http.ContentType.JSON;

public class ProductStep {

    public static final ProductRequest PRODUCT_CREATE_REQUEST_스키야키 = new ProductRequest(
            "스키야키",
            BigDecimal.valueOf(11_900)
    );

    public static final ProductRequest PRODUCT_CREATE_REQUEST_우동 = new ProductRequest(
            "우동",
            BigDecimal.valueOf(8_900)
    );

    public static ProductRequest toRequest(final Product product) {
        return new ProductRequest(
                product.getName(),
                product.getPrice()
        );
    }

    public static Product 상품_생성_요청하고_상품_반환(final ProductRequest request) {
        final ExtractableResponse<Response> response = 상품_생성_요청(request);
        return response.jsonPath().getObject("", Product.class);
    }

    public static Long 상품_생성_요청하고_아이디_반환(final ProductRequest request) {
        final ExtractableResponse<Response> response = 상품_생성_요청(request);
        return response.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 상품_생성_요청(final ProductRequest request) {
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
