package kitchenpos.integration.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class ProductStep {

    public static ExtractableResponse<Response> 상품_생성_요청(final String request) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/products")
                .then()
                .extract();
    }

}
