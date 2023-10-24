package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ui.request.MenuGroupCreateRequest;
import kitchenpos.ui.response.MenuGroupResponse;

import static io.restassured.http.ContentType.JSON;

public class MenuGroupStep {

    public static final MenuGroupCreateRequest MENU_GROUP_REQUEST_일식 = new MenuGroupCreateRequest("일식");
    public static final MenuGroupCreateRequest MENU_GROUP_REQUEST_한식 = new MenuGroupCreateRequest("한식");

    public static MenuGroupResponse 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(final MenuGroupCreateRequest request) {
        final ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(request);
        return response.jsonPath().getObject("", MenuGroupResponse.class);
    }

    public static Long 메뉴_그룹_생성_요청하고_아이디_반환(final MenuGroupCreateRequest request) {
        final ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(request);
        return response.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(final MenuGroupCreateRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

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
