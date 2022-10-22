package kitchenpos.acceptance;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.common.Request;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("MenuAcceptance 는 ")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 생성해야 한다.")
    @Test
    void createMenu() {
        final Map<String, Object> productCreationRequestBody = Map.of(
                "name", "productName",
                "price", 1000);
        final Product product = Request.create("/api/products", productCreationRequestBody)
                .body()
                .jsonPath()
                .getObject(".", Product.class);

        final Map<String, Object> menuGroupCreationRequestBody = Map.of("name", "menuGroup");
        final MenuGroup menuGroup = Request.create("/api/menu-groups", menuGroupCreationRequestBody)
                .body()
                .jsonPath()
                .getObject(".", MenuGroup.class);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setProductId(product.getId());
        final Map<String, Object> menuCreationRequestBody = Map.ofEntries(
                entry("name", "menu"),
                entry("price", 1000),
                entry("menuGroupId", menuGroup.getId()),
                entry("menuProducts", List.of(menuProduct))
        );
        ExtractableResponse<Response> response = Request.create("/api/menus", menuCreationRequestBody);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("모든 메뉴들을 조회해야 한다.")
    @Test
    void getMenus() {
        final Map<String, Object> productCreationRequestBody = Map.of(
                "name", "productName",
                "price", 1000);
        final Product product = Request.create("/api/products", productCreationRequestBody)
                .body()
                .jsonPath()
                .getObject(".", Product.class);

        final Map<String, Object> menuGroupCreationRequestBody = Map.of("name", "menuGroup");
        final MenuGroup menuGroup = Request.create("/api/menu-groups", menuGroupCreationRequestBody)
                .body()
                .jsonPath()
                .getObject(".", MenuGroup.class);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setProductId(product.getId());
        final Map<String, Object> menuCreationRequestBody = Map.ofEntries(
                entry("name", "menu"),
                entry("price", 1000),
                entry("menuGroupId", menuGroup.getId()),
                entry("menuProducts", List.of(menuProduct))
        );
        Request.create("/api/menus", menuCreationRequestBody);

        List<Menu> menus = Request.get("/api/menus")
                .body()
                .jsonPath()
                .getList(".", Menu.class);

        assertThat(menus.size()).isEqualTo(1);
    }
}
