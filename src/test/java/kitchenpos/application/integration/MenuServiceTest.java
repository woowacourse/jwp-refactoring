package kitchenpos.application.integration;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends ApplicationIntegrationTest {
    private MenuGroup menuGroup;

    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupService.create(new MenuGroup("치킨"));
        final Product product1 = productService.create(new Product("후라이드", BigDecimal.valueOf(16000)));
        final Product product2 = productService.create(new Product("양념치킨", BigDecimal.valueOf(16000)));
        menuProduct1 = new MenuProduct(null, product1.getId(), 1);
        menuProduct2 = new MenuProduct(null, product2.getId(), 1);
    }

    @Test
    void create_menu() {
        //given
        final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);
        final BigDecimal price = BigDecimal.valueOf(1600000, 2);
        final Menu menu = new Menu("후라이드", price, menuGroup.getId(), menuProducts);

        //when
        final Menu createdMenu = menuService.create(menu);

        //then
        assertThat(createdMenu)
                .usingRecursiveComparison()
                .ignoringFields("id", "menuProducts.seq")
                .isEqualTo(menu);
    }

    @Test
    @Disabled
    void cannot_create_menu_with_empty_name() {
        //given
        final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);
        final BigDecimal price = BigDecimal.valueOf(16000.00);
        final Menu menu = new Menu(null, price, menuGroup.getId(), menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_menu_with_empty_price() {
        //given
        final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);
        final BigDecimal price = null;
        final Menu menu = new Menu("후라이드", price, menuGroup.getId(), menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_menu_with_negative_price() {
        //given
        final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);
        final BigDecimal price = BigDecimal.valueOf(-16000.00);
        final Menu menu = new Menu("후라이드", price, menuGroup.getId(), menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_menu_with_empty_menu_group_id() {
        //given
        final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);
        final BigDecimal price = BigDecimal.valueOf(16000.00);
        final Menu menu = new Menu("후라이드", price, null, menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_menu_with_invalid_menu_group_id() {
        //given
        final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);
        final BigDecimal price = BigDecimal.valueOf(16000.00);
        final Menu menu = new Menu("후라이드", price, 100L, menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Disabled
    void cannot_create_menu_with_empty_menu_products() {
        //given
        final List<MenuProduct> menuProducts = null;
        final BigDecimal price = BigDecimal.valueOf(16000.00);
        final Menu menu = new Menu("후라이드", price, menuGroup.getId(), menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list_menus() {
        //given
        final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);
        final BigDecimal price = BigDecimal.valueOf(16000.00);
        final Menu menu = new Menu("후라이드", price, menuGroup.getId(), menuProducts);
        final Menu createdMenu = menuService.create(menu);

        //when
        final List<Menu> menus = menuService.list();

        //then
        assertThat(menus)
                .hasSize(1)
                .extracting("id")
                .containsExactlyInAnyOrder(createdMenu.getId());
    }
}