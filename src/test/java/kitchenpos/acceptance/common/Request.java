package kitchenpos.acceptance.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.MediaType;

public class Request {

    public static ExtractableResponse<Response> create(final String url, final Map<String, Object> body) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String url) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
