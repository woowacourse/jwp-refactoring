package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ui.jpa.dto.menugroup.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String API = "/api/menu-groups";

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroup() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("name");

        ExtractableResponse<Response> response = HttpMethodFixture.httpPost(menuGroupCreateRequest, API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("메뉴 그룹을 모두 조회할 수 있다.")
    @Test
    void listMenuGroup() {
        ExtractableResponse<Response> response = HttpMethodFixture.httpGet(API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
