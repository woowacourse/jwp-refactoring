package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 생성자")
    @Nested
    class MenuConstructor {

        @DisplayName("가격이 null이라면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceIsNull() {
            // given
            MenuProduct menuProduct = new MenuProduct();

            // when & then
            assertThatThrownBy(() -> new Menu("메뉴", null, 1L, List.of(menuProduct)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0보다 작다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceIsLessThan0() {
            // given
            MenuProduct menuProduct = new MenuProduct();

            // when & then
            assertThatThrownBy(() -> new Menu("메뉴", BigDecimal.valueOf(-1), 1L, List.of(menuProduct)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
