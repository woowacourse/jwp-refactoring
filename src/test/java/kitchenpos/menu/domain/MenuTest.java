package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.exception.InvalidMenuException;
import kitchenpos.menu.exception.InvalidMenuNameException;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Menu 단위 테스트")
class MenuTest {

    private static final Long MENU_GROUP_ID = 1L;

    @DisplayName("Menu를 생성할 때")
    @Nested
    class Create {

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // when, then
            assertThatThrownBy(() -> new Menu(null, BigDecimal.valueOf(11_900), MENU_GROUP_ID))
                .isExactlyInstanceOf(InvalidMenuNameException.class);
        }

        @DisplayName("name이 공백으로 이루어진 경우 예외가 발생한다.")
        @Test
        void nameBlankException() {
            // when, then
            assertThatThrownBy(() -> new Menu(" ", BigDecimal.valueOf(11_900), MENU_GROUP_ID))
                .isExactlyInstanceOf(InvalidMenuNameException.class);
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // when, then
            assertThatThrownBy(() -> new Menu("스테커3 버거", null, MENU_GROUP_ID))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }

        @DisplayName("price가 음수인 경우 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // when, then
            assertThatThrownBy(() -> new Menu("스테커3 버거", BigDecimal.valueOf(-1), MENU_GROUP_ID))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }

        @DisplayName("menuGroup이 Null인 경우 예외가 발생한다.")
        @Test
        void menuGroupIdNullException() {
            // when, then
            assertThatThrownBy(() -> new Menu("스테커3 버거", BigDecimal.valueOf(12_900), null))
                .isExactlyInstanceOf(InvalidMenuException.class);
        }
    }
}
