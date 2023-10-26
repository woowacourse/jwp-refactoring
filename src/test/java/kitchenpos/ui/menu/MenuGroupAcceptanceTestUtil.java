package kitchenpos.ui.menu;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.request.MenuGroupCreateRequest;
import kitchenpos.ui.ControllerAcceptanceTestHelper;
import kitchenpos.ui.menu.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public abstract class MenuGroupAcceptanceTestUtil extends ControllerAcceptanceTestHelper {

    protected MenuGroupCreateRequest 메뉴그룹_생성요청() {
        return new MenuGroupCreateRequest("커플메뉴");
    }

    protected ExtractableResponse<Response> 메뉴그룹을_생성한다(MenuGroupCreateRequest 요청) {
        return RestAssured.given().body(요청)
                .contentType(ContentType.JSON)
                .when().post("/api/menu-groups")
                .then()
                .extract();
    }

    protected void 메뉴그룹이_생성됨(MenuGroupCreateRequest 요청, ExtractableResponse<Response> 응답) {
        MenuGroupResponse response = 응답.as(MenuGroupResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(201);
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getName()).isEqualTo(요청.getName());
        });
    }

    protected ExtractableResponse<Response> 메뉴그룹_목록을_조회한다() {
        return RestAssured.given()
                .when().get("/api/menu-groups")
                .then()
                .extract();
    }

    protected void 메뉴그룹_목록이_조회됨(ExtractableResponse<Response> 응답) {
        List<MenuGroupResponse> responses = 응답.jsonPath().getList("", MenuGroupResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(200);
            softly.assertThat(responses).hasSize(1);
        });
    }
}
