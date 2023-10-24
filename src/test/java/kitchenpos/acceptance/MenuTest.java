package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance;
import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.fixture.MenuFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest extends Acceptance {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 메뉴를_생성한다() {
        // given
        CreateMenuRequest request = MenuFixture.REQUEST.후라이드_치킨_16000원_1마리_등록_요청();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/menus/1")
                .extract();
    }

    @Test
    void 메뉴를_전체_조회한다() {
        // given
        CreateMenuRequest request = MenuFixture.REQUEST.후라이드_치킨_16000원_1마리_등록_요청();
        Long menuId = MenuFixture.메뉴_생성(request);
        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/api/menus")
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.body().jsonPath().getList(".")).hasSize(1);
            softly.assertThat(response.body().jsonPath().getLong("[0].id")).isEqualTo(menuId);
            softly.assertThat(response.body().jsonPath().getString("[0].name")).isEqualTo(request.getName());
            softly.assertThat(new BigDecimal(response.body().jsonPath().getString("[0].price"))).isEqualByComparingTo(request.getPrice());
            softly.assertThat(response.body().jsonPath().getLong("[0].menuGroupId")).isEqualTo(request.getMenuGroupId());
        });

    }
}
