package kitchenpos.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

class MenuGroupAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void list() {
        get("/api/menu-groups").then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItems("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"));
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("menuGroup");

        // when
        ExtractableResponse<Response> response = post("/api/menu-groups", menuGroupRequest)
                .then().extract();
        MenuGroupResponse menuGroupResponse = response.as(MenuGroupResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(menuGroupResponse.getName()).isEqualTo(menuGroupRequest.getName());
    }
}
