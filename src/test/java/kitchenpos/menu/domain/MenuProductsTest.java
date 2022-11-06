package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    void 메뉴상품들_가격의_총합이_주어진_가격보다_작다면_MenuProducts를_생성할_수_없다() {
        // given
        final List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, 1L, BigDecimal.valueOf(1000), 1L),
                new MenuProduct(1L, 1L, BigDecimal.valueOf(1000), 1L)
        );

        // when & then
        assertThatThrownBy(() -> MenuProducts.of(menuProducts, BigDecimal.valueOf(2001)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴상품들_가격의_총합이_주어진_가격보다_크다면_MenuProducts를_생성할_수_있다() {
        // given
        final List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, 1L, BigDecimal.valueOf(1000), 2L),
                new MenuProduct(1L, 1L, BigDecimal.valueOf(1000), 1L)
        );

        // when & then
        assertThatCode(() -> MenuProducts.of(menuProducts, BigDecimal.valueOf(2999)))
                .doesNotThrowAnyException();
    }

    @Test
    void 메뉴상품들_가격의_총합이_주어진_가격보다_같다면_MenuProducts를_생성할_수_있다() {
        // given
        final List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, 1L, BigDecimal.valueOf(1000), 2L),
                new MenuProduct(1L, 1L, BigDecimal.valueOf(1000), 1L)
        );

        // when & then
        assertThatCode(() -> MenuProducts.of(menuProducts, BigDecimal.valueOf(3000)))
                .doesNotThrowAnyException();
    }
}
