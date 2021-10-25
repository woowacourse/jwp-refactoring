package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    void create() {
        Menu menu = menu();
        ExtractableResponse<Response> response = makeResponse("/api/menus", TestMethod.POST, menu);
        Menu created = response.body().as(Menu.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo("menu"),
            () -> assertThat(created.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @Test
    void list() {
        Menu menu = menu();
        makeResponse("/api/menus", TestMethod.POST, menu);

        List<Menu> menus = makeResponse("/api/menus", TestMethod.GET).jsonPath()
            .getList(".", Menu.class);
        assertThat(menus.size()).isEqualTo(1);
    }

    public Menu menu() {
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        MenuGroup createdMenuGroup = makeResponse("/api/menu-groups", TestMethod.POST, menuGroup)
            .as(MenuGroup.class);
        Product product = new Product("product", BigDecimal.valueOf(1000));
        Product createdProduct = makeResponse("/api/products", TestMethod.POST, product)
            .as(Product.class);
        MenuProduct menuProduct = new MenuProduct(createdProduct.getId(), 10);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

        return new Menu("menu", BigDecimal.valueOf(5000), createdMenuGroup.getId(), menuProducts);
    }
}