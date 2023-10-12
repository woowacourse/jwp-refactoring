package kitchenpos;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class MenuGroupIntegrationTest extends IntegrationTest {

    private static final MenuGroup MENU_GROUP_JAPANESE = new MenuGroup("일식");
    private static final MenuGroup MENU_GROUP_KOREAN = new MenuGroup("한식");

    @Test
    void 메뉴_그룹을_생성한다() {
        final MenuGroup menuGroup = MENU_GROUP_JAPANESE;

        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(menuGroup);

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(menuGroup.getName());
    }

    @Test
    void 메뉴_그룹을_조회한다() {
        final List<MenuGroup> menuGroups = List.of(MENU_GROUP_JAPANESE, MENU_GROUP_KOREAN);
        for (final MenuGroup menuGroup : menuGroups) {
            메뉴_그룹_생성_요청(menuGroup);
        }

        final ExtractableResponse<Response> response = 메뉴_그룹_조회_요청();
        final List<MenuGroup> result = response.jsonPath().getList("", MenuGroup.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(result.size()).isEqualTo(menuGroups.size()),
                () -> assertThat(result.get(0))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(menuGroups.get(0)),
                () -> assertThat(result.get(1))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(menuGroups.get(1))
        );
    }

    private static ExtractableResponse<Response> 메뉴_그룹_생성_요청(final MenuGroup menuGroup) {
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

    private static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
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
