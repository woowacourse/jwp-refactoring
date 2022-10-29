package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuTest {

    private final List<MenuProduct> menuProducts = List.of(
            new MenuProduct(1L, new BigDecimal(10_000), 1),
            new MenuProduct(2L, new BigDecimal(10_000), 1)
    );

    @Test
    void 메뉴_생성() {
        Assertions.assertDoesNotThrow(() -> new Menu("돈까스", new BigDecimal(20_000), 1L, menuProducts));
    }

    @Test
    void 상품의_가격의_합보다_메뉴의_가격의_합이_커야한다() {
        assertThatThrownBy(() -> new Menu("돈까스", new BigDecimal(20_001), 1L, menuProducts))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 값의 합보다 메뉴의 값이 낮을 수 없습니다.");
    }
}