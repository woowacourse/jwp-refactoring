package kitchenpos.acceptance.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LocationParser {
    public static Long extractCreatedId(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return extractCreatedId(uri);
    }

    private static Long extractCreatedId(String uri) {
        String[] uriToken = uri.split("/");
        return Long.valueOf(uriToken[uriToken.length - 1]);
    }
}
