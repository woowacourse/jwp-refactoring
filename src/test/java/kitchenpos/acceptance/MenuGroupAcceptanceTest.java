package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final String menuGroupName = "한마리메뉴";
        final MenuGroup menuGroup = new MenuGroup(menuGroupName);

        // when
        final MenuGroup response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().log().all()
                .post("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MenuGroup.class);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(menuGroupName);
    }

    @DisplayName("상품 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        final List<MenuGroup> menuGroups = getMenuGroups(response);

        // then
        assertThat(menuGroups)
                .hasSize(4)
                .filteredOn(it -> it.getId() != null)
                .extracting(MenuGroup::getName)
                .containsExactlyInAnyOrder(
                        "두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"
                );
    }

    private static List<MenuGroup> getMenuGroups(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", MenuGroup.class);
    }
}
