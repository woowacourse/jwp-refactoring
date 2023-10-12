package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;

import static io.restassured.http.ContentType.JSON;

public class MenuStep {

    public static Long 메뉴_생성_요청하고_아이디_반환(final Menu menu) {
        final ExtractableResponse<Response> response = 메뉴_생성_요청(menu);
        return response.jsonPath().getLong("id");
    }

    public static Menu 메뉴_생성_요청하고_메뉴_반환(final Menu menu) {
        final ExtractableResponse<Response> response = 메뉴_생성_요청(menu);
        return response.jsonPath().getObject("", Menu.class);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(final Menu menu) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(menu)

                .when()
                .post("/api/menus")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .get("/api/menus")

                .then()
                .log().all()
                .extract();
    }
}
