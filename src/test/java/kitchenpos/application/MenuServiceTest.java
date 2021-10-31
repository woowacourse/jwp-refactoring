package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import static org.assertj.core.api.Assertions.assertThat;

class MenuServiceTest extends ServiceTest {

    private final Menu menu;

    public MenuServiceTest() {
        final List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(null, 1L, 1L, 1L));
        this.menu = new Menu(null, "메뉴", BigDecimal.valueOf(10000), 1L, menuProducts);
    }

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 생성")
    void createTest() {

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertThat(menuService.list()).contains(savedMenu);
    }
}
