package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.acceptance.common.httpcommunication.MenuGroupHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.MenuHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.ProductHttpCommunication;
import kitchenpos.common.fixture.RequestBody;
import kitchenpos.menu.ui.dto.response.MenuGroupResponse;
import kitchenpos.menu.ui.dto.response.MenuResponse;
import kitchenpos.product.ui.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("MenuAcceptance 는 ")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 생성해야 한다.")
    @Test
    void createMenu() {
        final ProductResponse product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponseBodyAsObject(ProductResponse.class);

        final MenuGroupResponse menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponseBodyAsObject(MenuGroupResponse.class);

        final ExtractableResponse<Response> response = MenuHttpCommunication.create(
                        RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                .getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("모든 메뉴들을 조회해야 한다.")
    @Test
    void getMenus() {
        final ProductResponse product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponseBodyAsObject(ProductResponse.class);

        final MenuGroupResponse menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponseBodyAsObject(MenuGroupResponse.class);

        MenuHttpCommunication.create(RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()));

        final List<MenuResponse> menus = MenuHttpCommunication.getMenus()
                .getResponseBodyAsList(MenuResponse.class);

        assertThat(menus.size()).isEqualTo(1);
    }
}
