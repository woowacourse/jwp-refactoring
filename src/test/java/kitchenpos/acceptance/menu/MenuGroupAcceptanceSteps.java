package kitchenpos.acceptance.menu;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.MenuGroupCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupAcceptanceSteps {

    public static Long 메뉴_그릅_등록후_생성된_ID를_가져온다(String 메뉴_그룹_이름) {
        return 생성된_ID를_추출한다(메뉴_그릅_등록_요청을_보낸다(메뉴_그룹_이름));
    }

    public static ExtractableResponse<Response> 메뉴_그릅_등록_요청을_보낸다(String 메뉴_그룹_이름) {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest(메뉴_그룹_이름);
        return given()
                .body(request)
                .post("/api/menu-groups")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청을_보낸다() {
        return given()
                .get("/api/menu-groups")
                .then()
                .log().headers()
                .extract();
    }
}
