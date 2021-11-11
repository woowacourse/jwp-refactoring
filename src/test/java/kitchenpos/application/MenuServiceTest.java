package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.Fixtures;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuServiceTest extends IntegrationTest {

    @Test
    void create() {
        // given
        MenuProduct menuProduct = Fixtures.menuProduct(1, 1, 1, 1);
        Menu menu = Fixtures.menu(1, "메뉴", 5000, 1, Arrays.asList(menuProduct));

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        List<MenuProduct> menuProducts = savedMenu.getMenuProducts();
        assertThat(menuProducts).hasSize(1);
    }

    @Test
    void list() {
        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(6);
    }
}