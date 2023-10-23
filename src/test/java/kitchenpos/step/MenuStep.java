package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductCreateRequest;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.http.ContentType.JSON;

public class MenuStep {

    public static MenuCreateRequest MENU_CREATE_REQUEST_스키야키(BigDecimal price, final Long menuGroupId, final List<MenuProductCreateRequest> menuProducts) {
        return new MenuCreateRequest(
                "스키야키",
                price,
                menuGroupId,
                menuProducts
        );
    }

    public static Long 메뉴_생성_요청하고_아이디_반환(final MenuCreateRequest request) {
        final ExtractableResponse<Response> response = 메뉴_생성_요청(request);
        return response.jsonPath().getLong("id");
    }

    public static Menu 메뉴_생성_요청하고_메뉴_반환(final MenuCreateRequest request) {
        final ExtractableResponse<Response> response = 메뉴_생성_요청(request);
        return response.jsonPath().getObject("", Menu.class);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(final MenuCreateRequest request) {
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
