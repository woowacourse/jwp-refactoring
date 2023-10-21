package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.request.MenuGroupRequest;

import static io.restassured.http.ContentType.JSON;

public class MenuGroupStep {

    public static MenuGroupRequest toRequest(final MenuGroup menuGroup) {
        return new MenuGroupRequest(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }

    public static Long 메뉴_그룹_생성_요청하고_아이디_반환(final MenuGroupRequest request) {
        final ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(request);
        return response.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(final MenuGroupRequest request) {
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
