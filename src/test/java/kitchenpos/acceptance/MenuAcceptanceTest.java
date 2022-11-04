package kitchenpos.acceptance;

import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.menu.ui.dto.menu.MenuCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuAcceptanceTest extends AcceptanceTest {

    private static final String API = "/api/menus";

    @DisplayName("메뉴를 생성할 수 있다")
    @Test
    void create() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("name", 1000L, 1L, List.of(MENU_PRODUCT_1.getId()));

        ExtractableResponse<Response> response = HttpMethodFixture.httpPost(menuCreateRequest, API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("메뉴를 모두 조회한다.")
    @Test
    void list() {
        ExtractableResponse<Response> response = HttpMethodFixture.httpGet(API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
