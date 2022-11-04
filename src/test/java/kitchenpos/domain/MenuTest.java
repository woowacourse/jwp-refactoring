package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴를_생성한다() {
        //given
        final BigDecimal price = BigDecimal.valueOf(2000);
        final List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, 2L, BigDecimal.valueOf(500L)),
                new MenuProduct(1L, 2L, BigDecimal.valueOf(500L))
        );

        // when & then
        assertThatCode(() -> new Menu("메뉴", price, 1L,
                menuProducts))
                .doesNotThrowAnyException();
    }

    @Test
    void 상품가격의_총합보다_메뉴의_가격이_더_크다면_메뉴를_생성할_수_없다() {
        //given
        final BigDecimal price = BigDecimal.valueOf(2001L);
        final List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, 2L, BigDecimal.valueOf(500L)),
                new MenuProduct(1L, 2L, BigDecimal.valueOf(500L))
        );

        // when & then
        assertThatThrownBy(
                () -> new Menu("메뉴", price, 1L,
                menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
