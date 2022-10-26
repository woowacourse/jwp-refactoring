package kitchenpos.application;

import static kitchenpos.Fixture.메뉴의_상품은;
import static kitchenpos.Fixture.메뉴집합;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() {
        MenuProduct 햄버거1 = 메뉴의_상품은(상품_생성(100_000));
        MenuProduct 햄버거2 = 메뉴의_상품은(상품_생성(100_000));
        Menu menu = new Menu("햄버억", new BigDecimal(200_000), 메뉴집합_생성().getId(), List.of(햄버거1, 햄버거2));

        Menu actual = menuService.create(menu);
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 생성할때_가격이_존재하지_않는_경우_예외를_발생시킨다() {
        MenuProduct 햄버거1 = 메뉴의_상품은(상품_생성(100_000));
        MenuProduct 햄버거2 = 메뉴의_상품은(상품_생성(100_000));
        Menu menu = new Menu("햄버억", null, 메뉴집합().getId(), List.of(햄버거1, 햄버거2));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_가격이_0보다_작은_경우_예외를_발생시킨다() {
        MenuProduct 햄버거1 = 메뉴의_상품은(상품_생성(100_000));
        MenuProduct 햄버거2 = 메뉴의_상품은(상품_생성(100_000));
        Menu menu = new Menu("햄버억", new BigDecimal(-1), 메뉴집합().getId(), List.of(햄버거1, 햄버거2));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_메뉴그룹이_존재하지_않는_경우_예외를_발생시킨다() {
        MenuProduct 햄버거1 = 메뉴의_상품은(상품_생성(100_000));
        MenuProduct 햄버거2 = 메뉴의_상품은(상품_생성(100_000));
        Menu menu = new Menu("햄버억", new BigDecimal(200_000), -1L, List.of(햄버거1, 햄버거2));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_상품이_존재하지_않는_경우_예외를_발생시킨다() {
        MenuProduct 햄버거1 = 메뉴의_상품은(상품_생성(100_000));
        MenuProduct 존재하지_않는_상품 = new MenuProduct(-1L, 1);
        Menu menu = new Menu("햄버억", new BigDecimal(200_000), 메뉴집합().getId(), List.of(햄버거1, 존재하지_않는_상품));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_상품의_가격의_합과_메뉴의_가격이_다를_경우_예외를_발생시킨다() {
        MenuProduct 햄버거1 = 메뉴의_상품은(상품_생성(100_000));
        MenuProduct 햄버거2 = 메뉴의_상품은(상품_생성(100_000));
        Menu menu = new Menu("햄버억", new BigDecimal(200_001), 메뉴집합().getId(), List.of(햄버거1, 햄버거2));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_조회한다() {
        메뉴_생성();

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSizeGreaterThanOrEqualTo(1);
    }
}
