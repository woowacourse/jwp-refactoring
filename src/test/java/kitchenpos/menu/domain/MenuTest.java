package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴의 가격이 null인 경우 예외 발생")
    void whenMenuPriceIsNull() {
        assertThatThrownBy(() -> createMenu(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 음수일 경우 예외 발생")
    void whenMenuPriceIsNegative() {
        final BigDecimal negativePrice = BigDecimal.valueOf(-1000);

        assertThatThrownBy(() -> createMenu(negativePrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴의 상품가격의 합보다 비싼 경우 예외 발생")
    void whenMenuProductsIsMoreExpensivePrice() {
        assertThatThrownBy(() -> new Menu(
                1L,
                "맥북",
                BigDecimal.valueOf(2_000_000L),
                1L,
                List.of(new MenuProduct(1L, 1L, BigDecimal.valueOf(20_000L)))
        )).isInstanceOf(IllegalArgumentException.class);
    }

    private Menu createMenu(final BigDecimal price) {
        return new Menu(1L, "맥북", price, 1L);
    }
}
