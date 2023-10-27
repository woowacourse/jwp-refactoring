package kitchenpos.acceptance;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SuppressWarnings("NonAsciiCharacters")
public class AcceptanceSteps {

    public static RequestSpecification given() {
        return RestAssured
                .given().log().all()
                .contentType(JSON);
    }

    public static void 응답_상태를_검증한다(
            ExtractableResponse<Response> 응답,
            int 예상_상태_코드
    ) {
        assertThat(응답.statusCode()).isEqualTo(예상_상태_코드);
    }

    public static Long 생성된_ID를_추출한다(
            ExtractableResponse<Response> 응답
    ) {
        String location = 응답.header("Location");
        return Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
    }

    public static void 조회_요청_결과를_검증한다(
            ExtractableResponse<Response> 응답,
            String 예상
    ) {
        assertThat(응답.response().body().prettyPrint())
                .isEqualTo(예상);
    }
}
