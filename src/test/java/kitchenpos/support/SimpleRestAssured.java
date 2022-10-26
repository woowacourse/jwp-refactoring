package kitchenpos.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import org.springframework.http.MediaType;

public class SimpleRestAssured {

    public static ExtractableResponse<Response> get(String path) {
        return thenExtract(given()
                .when().get(path));
    }

    public static ExtractableResponse<Response> post(final String path, final Object request) {
        return thenExtract(given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path));
    }

    private static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    private static ExtractableResponse<Response> thenExtract(final Response response) {
        return response
                .then().log().all()
                .extract();
    }

    public static <T> List<T> toObjectList(final ExtractableResponse<Response> response, final Class<T> clazz) {
        return response.body().jsonPath().getList(".", clazz);
    }
}
