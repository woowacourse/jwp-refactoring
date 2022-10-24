package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        final Menu menu = new Menu("메뉴", BigDecimal.valueOf(25000), 1L, List.of(new MenuProduct(null, 1L, 5)));

        final Menu persistedMenu = menuService.create(menu);

        assertThat(persistedMenu.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴의 가격이 null인 경우 예외 발생")
    void whenMenuPriceIsNull() {
        final Menu menu = new Menu("메뉴", null, 1L, List.of(new MenuProduct(null, 1L, 5)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 음수일 경우 예외 발생")
    void whenMenuPriceIsNegative() {
        final Menu menu = new Menu("메뉴", BigDecimal.valueOf(-10), 1L, List.of(new MenuProduct(null, 1L, 5)));
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 올바르지 않은 경우 예외 발생")
    void whenInvalidMenuGroup() {
        long invalidMenuGroupId = 99999L;
        final Menu menu = new Menu("메뉴", BigDecimal.valueOf(25000), invalidMenuGroupId,
                List.of(new MenuProduct(null, 1L, 5)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 올바르지 않은 경우 예외 발생")
    void whenInvalidProduct() {
        long invalidProductId = 99999L;
        final Menu menu = new Menu("메뉴", BigDecimal.valueOf(25000), 1L,
                List.of(new MenuProduct(null, invalidProductId, 5)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴의 상품가격의 합보다 비싼 경우 예외 발생")
    void whenMenuProductsIsMoreExpensivePrice() {
        final Menu menu = new Menu("메뉴", BigDecimal.valueOf(25000), 1L, List.of(new MenuProduct(null, 1L, 1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 목록을 가져온다.")
    void getList() {
        final List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(6);
    }
}
