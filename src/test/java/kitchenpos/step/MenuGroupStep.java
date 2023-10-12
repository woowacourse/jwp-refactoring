package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

import static io.restassured.http.ContentType.JSON;

public class MenuGroupStep {

    public static final MenuGroup MENU_GROUP_JAPANESE = new MenuGroup("일식");
    public static final MenuGroup MENU_GROUP_KOREAN = new MenuGroup("한식");

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(final MenuGroup menuGroup) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(menuGroup)

                .when()
                .post("/api/menu-groups")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .get("/api/menu-groups")

                .then()
                .log().all()
                .extract();
    }
}
