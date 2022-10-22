package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.acceptance.common.Request;
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
        final Product product = Request.create("/api/products", RequestBody.PRODUCT)
                .body()
                .jsonPath()
                .getObject(".", Product.class);

        final MenuGroup menuGroup = Request.create("/api/menu-groups", RequestBody.MENU_GROUP)
                .body()
                .jsonPath()
                .getObject(".", MenuGroup.class);

        ExtractableResponse<Response> response = Request.create("/api/menus",
                RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("모든 메뉴들을 조회해야 한다.")
    @Test
    void getMenus() {
        final Product product = Request.create("/api/products", RequestBody.PRODUCT)
                .body()
                .jsonPath()
                .getObject(".", Product.class);

        final MenuGroup menuGroup = Request.create("/api/menu-groups", RequestBody.MENU_GROUP)
                .body()
                .jsonPath()
                .getObject(".", MenuGroup.class);

        Request.create("/api/menus", RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()));

        List<Menu> menus = Request.get("/api/menus")
                .body()
                .jsonPath()
                .getList(".", Menu.class);

        assertThat(menus.size()).isEqualTo(1);
    }
}
