package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

class HttpMethodFixture {

    private HttpMethodFixture() {
        throw new IllegalStateException("객체 생성 불가 메서드");
    }

    public static ExtractableResponse<Response> httpGet(String path) {
        return RestAssured
                .given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPost(Object postRequest, String path) {
        return RestAssured
                .given().log().all()
                .body(postRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPut(Object putRequest, String path) {
        return RestAssured
                .given().log().all()
                .body(putRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpDelete(String path) {
        return RestAssured
                .given().log().all()
                .when().delete(path)
                .then().log().all()
                .extract();
    }
}
