package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class MenuServiceTest {

    private final MenuService menuService;

    MenuServiceTest(MenuService menuService) {
        this.menuService = menuService;
    }

    @Test
    void 메뉴를_생성한다() {
        MenuProduct 후라이드 = new MenuProduct(1L, 7L, 1L, 1);
        MenuProduct 양념 = new MenuProduct(2L, 7L, 2L, 1);
        Menu menu = new Menu("반반치킨", new BigDecimal(32_000), 1L, List.of(후라이드, 양념));

        assertThat(menuService.create(menu)).isInstanceOf(Menu.class);
    }

    @Test
    void 생성할때_가격이_존재하지_않는_경우_예외를_발생시킨다() {
        MenuProduct 후라이드 = new MenuProduct(1L, 7L, 1L, 1);
        MenuProduct 양념 = new MenuProduct(2L, 7L, 2L, 1);
        Menu menu = new Menu("반반치킨", null, 1L, List.of(후라이드, 양념));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_가격이_0보다_작은_경우_예외를_발생시킨다() {
        MenuProduct 후라이드 = new MenuProduct(1L, 7L, 1L, 1);
        MenuProduct 양념 = new MenuProduct(2L, 7L, 2L, 1);
        Menu menu = new Menu("반반치킨", new BigDecimal(-1), 1L, List.of(후라이드, 양념));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_메뉴그룹이_존재하지_않는_경우_예외를_발생시킨다() {
        MenuProduct 후라이드 = new MenuProduct(1L, 7L, 1L, 1);
        MenuProduct 양념 = new MenuProduct(2L, 7L, 2L, 1);
        Menu menu = new Menu("반반치킨", new BigDecimal(32_000), -1L, List.of(후라이드, 양념));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_상품이_존재하지_않는_경우_예외를_발생시킨다() {
        MenuProduct 후라이드 = new MenuProduct(1L, 7L, 1L, 1);
        MenuProduct 존재하지않는메뉴 = new MenuProduct(2L, 7L, -1L, 1);
        Menu menu = new Menu("반반치킨", new BigDecimal(32_000), -1L, List.of(후라이드, 존재하지않는메뉴));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_상품의_가격의_합과_메뉴의_가격이_다를_경우_예외를_발생시킨다() {
        MenuProduct 후라이드 = new MenuProduct(1L, 7L, 1L, 1);
        MenuProduct 양념 = new MenuProduct(2L, 7L, 2L, 1);
        Menu menu = new Menu("반반치킨", new BigDecimal(32_001), 1L, List.of(후라이드, 양념));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_조회한다() {
        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(6);
    }
}
