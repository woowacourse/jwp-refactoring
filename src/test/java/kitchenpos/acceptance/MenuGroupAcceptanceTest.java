package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.acceptance.common.MenuGroupHttpCommunication;
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
        final ExtractableResponse<Response> response = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("메뉴 그룹들을 가져온다.")
    @Test
    void getMenuGroups() {
        MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP);

        final List<MenuGroup> menuGroups = MenuGroupHttpCommunication.getMenuGroups()
                .getResponseBodyAsList(MenuGroup.class);

        assertThat(menuGroups.size()).isEqualTo(1);
    }
}
