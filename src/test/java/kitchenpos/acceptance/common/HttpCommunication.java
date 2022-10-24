package kitchenpos.acceptance.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

public class HttpCommunication {

    private final ExtractableResponse<Response> response;

    public HttpCommunication(final ExtractableResponse<Response> response) {
        this.response = response;
    }

    public static RequestBuilder request() {
        return new RequestBuilder();
    }

    public ExtractableResponse<Response> getResponse() {
        return response;
    }

    public <T> List<T> getResponseBodyAsList(Class<T> returnType) {
        return response.body()
                .jsonPath()
                .getList(".", returnType);
    }

    public <T> T getResponseBodyAsObject(Class<T> returnType) {
        return response.body()
                .jsonPath()
                .getObject(".", returnType);
    }

    public static class RequestBuilder {

        private ExtractableResponse<Response> response;

        public RequestBuilder create(final String url, final Map<String, Object> body) {
            response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                    .when().post(url)
                    .then().log().all()
                    .extract();
            return this;
        }

        public RequestBuilder get(final String url) {
            response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get(url)
                    .then().log().all()
                    .extract();
            return this;
        }

        public RequestBuilder update(final String url, final Map<String, Object> body) {
            response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                    .when().put(url)
                    .then().log().all()
                    .extract();
            return this;
        }

        public RequestBuilder delete(final String url) {
            response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete(url)
                    .then().log().all()
                    .extract();
            return this;
        }


        public HttpCommunication build() {
            return new HttpCommunication(response);
        }
    }
}
