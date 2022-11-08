package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import kitchenpos.menu.application.request.MenuGroupRequest;
import kitchenpos.menu.application.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final String menuGroupName = "한마리메뉴";
        final MenuGroupRequest request = new MenuGroupRequest(menuGroupName);

        // when
        final MenuGroupResponse response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when().log().all()
                .post("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MenuGroupResponse.class);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(menuGroupName);
    }

    @DisplayName("상품 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<MenuGroupResponse> menuGroups = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", MenuGroupResponse.class);

        // then
        assertThat(menuGroups)
                .hasSize(4)
                .filteredOn(it -> it.getId() != null)
                .extracting(MenuGroupResponse::getName)
                .containsExactlyInAnyOrder(
                        "두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"
                );
    }
}
