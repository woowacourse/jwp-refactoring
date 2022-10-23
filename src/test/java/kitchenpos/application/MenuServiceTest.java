package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    void create() {
        MenuProduct menuProduct = MenuProduct.of(1L, 1L, 10);
        Menu menu = Menu.of("name", BigDecimal.valueOf(1000), 1L, List.of(menuProduct));

        Long savedId = menuService.create(menu);

        assertThat(savedId).isNotNull();
    }

    @Test
    void createThrowExceptionWhenPriceIsNegative() {
        Menu menu = Menu.of("name", BigDecimal.valueOf(-100), 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("product의 가격은 음수가 될 수 없습니다.");
    }

    @Test
    void createThrowExceptionWhenNotExistProduct() {
        MenuProduct menuProduct = MenuProduct.of( 0L, 0L, 10);
        Menu menu = Menu.of("name", BigDecimal.valueOf(100), 1L, List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 product입니다.");
    }

    @Test
    void createThrowExceptionWhenNotExistMenuId() {
        MenuProduct menuProduct = MenuProduct.of(1L, 1L, 10);
        Menu menu = Menu.of("name", BigDecimal.valueOf(100), 0L, List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("menuGroup이 존재하지 않습니다.");
    }

    @Test
    void list() {
        List<Menu> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(6);
    }
}
