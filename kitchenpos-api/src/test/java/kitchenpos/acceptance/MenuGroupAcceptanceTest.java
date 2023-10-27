package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.application.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest("가을 특선 메뉴");

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(menuGroup)

                .when()
                .post("/api/menu-groups")

                .then()
                .log().all()
                .extract();

        // then
        MenuGroup actual = response.body().as(MenuGroup.class);

        assertThat(actual).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @Test
    void 메뉴_그룹_전체_목록을_조회한다() {
        // given
        MenuGroup menuGroup = new MenuGroup("가을 특선 메뉴");

        RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(menuGroup)

                .when()
                .post("/api/menu-groups")

                .then()
                .log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .get("/api/menu-groups")

                .then()
                .log().all()
                .extract();

        // then
        List<MenuGroup> actual = response.jsonPath().getList("", MenuGroup.class);

        assertThat(actual).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(List.of(menuGroup));
    }
}
