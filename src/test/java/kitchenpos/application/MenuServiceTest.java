package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("메뉴 서비스 테스트")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        Menu created = menuService.create(menu());

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo("menu"),
            () -> assertThat(created.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @DisplayName("메뉴 리스트를 불러온다.")
    @Test
    void list() {
        menuService.create(menu());

        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(1);
    }

    public Menu menu() {
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);
        Product product = new Product("product", BigDecimal.valueOf(1000));
        Product createdProduct = productService.create(product);
        MenuProduct menuProduct = new MenuProduct(createdProduct.getId(), 10);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

        return new Menu("menu", BigDecimal.valueOf(5000), createdMenuGroup.getId(), menuProducts);
    }
}