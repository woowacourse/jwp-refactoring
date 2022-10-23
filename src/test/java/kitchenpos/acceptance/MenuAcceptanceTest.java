package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.acceptance.common.MenuGroupHttpCommunication;
import kitchenpos.acceptance.common.MenuHttpCommunication;
import kitchenpos.acceptance.common.ProductHttpCommunication;
import kitchenpos.acceptance.common.fixture.RequestBody;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("MenuAcceptance 는 ")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 생성해야 한다.")
    @Test
    void createMenu() {
        final Product product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponseBodyAsObject(Product.class);

        final MenuGroup menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponseBodyAsObject(MenuGroup.class);

        final ExtractableResponse<Response> response = MenuHttpCommunication.create(
                        RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                .getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("모든 메뉴들을 조회해야 한다.")
    @Test
    void getMenus() {
        final Product product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponseBodyAsObject(Product.class);

        final MenuGroup menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponseBodyAsObject(MenuGroup.class);

        MenuHttpCommunication.create(RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()));

        final List<Menu> menus = MenuHttpCommunication.getMenus()
                .getResponseBodyAsList(Menu.class);

        assertThat(menus.size()).isEqualTo(1);
    }
}
