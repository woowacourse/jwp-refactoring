package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.ui.request.MenuRequest;

import static io.restassured.http.ContentType.JSON;

public class MenuStep {

    public static MenuRequest toMenuRequest(final Menu menu) {
        return new MenuRequest(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menu.getMenuProducts()
        );
    }

    public static Long 메뉴_생성_요청하고_아이디_반환(final Menu menu) {
        final ExtractableResponse<Response> response = 메뉴_생성_요청(toMenuRequest(menu));
        return response.jsonPath().getLong("id");
    }

    public static Menu 메뉴_생성_요청하고_메뉴_반환(final MenuRequest request) {
        final ExtractableResponse<Response> response = 메뉴_생성_요청(request);
        return response.jsonPath().getObject("", Menu.class);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(final MenuRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

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