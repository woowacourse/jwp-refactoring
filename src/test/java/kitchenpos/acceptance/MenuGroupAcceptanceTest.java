package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.common.Request;
import kitchenpos.acceptance.common.fixture.RequestBody;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("MenuGroupAcceptance 는 ")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        ExtractableResponse<Response> response = Request.create("/api/menu-groups", RequestBody.MENU_GROUP);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("메뉴 그룹들을 가져온다.")
    @Test
    void getMenuGroups() {
        Request.create("/api/menu-groups", RequestBody.MENU_GROUP);

        List<MenuGroup> menuGroups = Request.get("/api/menu-groups")
                .body()
                .jsonPath()
                .getList(".", MenuGroup.class);

        assertThat(menuGroups.size()).isEqualTo(1);
    }
}
