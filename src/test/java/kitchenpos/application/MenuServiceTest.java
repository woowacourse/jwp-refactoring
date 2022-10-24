package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    private MenuService menuService;

    @Autowired
    public MenuServiceTest(MenuService menuService) {
        this.menuService = menuService;
    }

    @Test
    void create() {
        // given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(30000), 1L, List.of(new MenuProduct(null, 1L, 2)));
        // when
        Menu createdMenu = menuService.create(menu);
        // then
        assertThat(createdMenu.getId()).isNotNull();
    }

    @Test
    void createMenuWithNullPrice() {
        // given
        Menu menu = new Menu("메뉴", null, 1L, List.of(new MenuProduct(null, 1L, 2)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithNegativePrice() {
        // given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(-1), 1L, List.of(new MenuProduct(null, 1L, 2)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithInvalidMenuGroup() {
        // given
        long invalidMenuGroupId = 999L;
        Menu menu = new Menu(
                "메뉴",
                BigDecimal.valueOf(30000),
                invalidMenuGroupId,
                List.of(new MenuProduct(null, 1L, 2))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithInvalidProduct() {
        // given
        long invalidProductId = 999L;
        Menu menu = new Menu(
                "메뉴",
                BigDecimal.valueOf(30000),
                1L,
                List.of(new MenuProduct(null, invalidProductId, 2))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithMoreExpensivePrice() {
        // given
        Menu menu = new Menu("메뉴", BigDecimal.valueOf(33000), 1L, List.of(new MenuProduct(null, 1L, 2)));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given & when
        List<Menu> menus = menuService.list();
        // then
        int defaultSize = 6;
        assertThat(menus).hasSize(defaultSize);
    }
}
