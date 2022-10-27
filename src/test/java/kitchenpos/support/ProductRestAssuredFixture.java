package kitchenpos.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ProductRestAssuredFixture {

    private static final String BASE_PATH = "/api/products";

    public static ExtractableResponse<Response> 상품_생성_요청(final Object request) {
        return SimpleRestAssured.post(BASE_PATH, request);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return SimpleRestAssured.get(BASE_PATH);
    }
}
