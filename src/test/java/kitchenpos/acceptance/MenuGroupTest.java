package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance;
import kitchenpos.application.dto.request.CreateMenuGroupRequest;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static kitchenpos.fixture.MenuGroupFixture.REQUEST;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupTest extends Acceptance {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        CreateMenuGroupRequest request = REQUEST.메뉴_그룹_치킨_생성_요청();
        // when & then
        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/api/menu-groups")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/menu-groups/1");
    }

    @Test
    void 메뉴_그룹을_조회한다() {
        // given
        CreateMenuGroupRequest request = REQUEST.메뉴_그룹_치킨_생성_요청();
        Long menuGroupId = MenuGroupFixture.메뉴_그룹_생성(request);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/api/menu-groups")
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.body().jsonPath().getList(".")).hasSize(1);
            softly.assertThat(response.body().jsonPath().getLong("[0].id")).isEqualTo(menuGroupId);
            softly.assertThat(response.body().jsonPath().getString("[0].name")).isEqualTo(request.getName());
        });
    }
}
