package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;

import static io.restassured.http.ContentType.JSON;

public class ProductStep {

    public static Long 상품_생성_요청하고_아이디_반환(final Product product) {
        final ExtractableResponse<Response> response = 상품_생성_요청(product);
        return response.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 상품_생성_요청(final Product product) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(product)

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
