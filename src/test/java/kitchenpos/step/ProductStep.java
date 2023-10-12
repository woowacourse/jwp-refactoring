package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

import static io.restassured.http.ContentType.JSON;

public class ProductStep {

    public static Product 스키야키() {
        final Product product = new Product();
        product.setName("스키야키");
        product.setPrice(BigDecimal.valueOf(11_900));
        return product;
    }

    public static Product 우동() {
        final Product product = new Product();
        product.setName("우동");
        product.setPrice(BigDecimal.valueOf(8_900));
        return product;
    }

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
