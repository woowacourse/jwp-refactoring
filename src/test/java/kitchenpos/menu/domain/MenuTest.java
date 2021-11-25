package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.exception.InvalidMenuNameException;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.exception.InvalidMenuException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Menu 단위 테스트")
class MenuTest {

    @DisplayName("Menu를 생성할 때")
    @Nested
    class Create {

        @DisplayName("정상적인 Menu는 저장에 성공한다.")
        @Test
        void success() {
            // given
            MenuGroup savedMenuGroup = new MenuGroup("버거킹 세트");

            // when
            assertThatCode(() -> new Menu("스테커2 버거", BigDecimal.valueOf(11_900), savedMenuGroup))
                .doesNotThrowAnyException();
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            MenuGroup savedMenuGroup = new MenuGroup("버거킹 세트");

            // when, then
            assertThatThrownBy(() -> new Menu(null, BigDecimal.valueOf(11_900), savedMenuGroup))
                .isExactlyInstanceOf(InvalidMenuNameException.class);
        }

        @DisplayName("name이 공백으로 이루어진 경우 예외가 발생한다.")
        @Test
        void nameBlankException() {
            // given
            MenuGroup savedMenuGroup = new MenuGroup("버거킹 세트");

            // when, then
            assertThatThrownBy(() -> new Menu(" ", BigDecimal.valueOf(11_900), savedMenuGroup))
                .isExactlyInstanceOf(InvalidMenuNameException.class);
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            MenuGroup savedMenuGroup = new MenuGroup("버거킹 세트");

            // when, then
            assertThatThrownBy(() -> new Menu("스테커3 버거", null, savedMenuGroup))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }

        @DisplayName("price가 음수인 경우 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // given
            MenuGroup savedMenuGroup = new MenuGroup("버거킹 세트");

            // when, then
            assertThatThrownBy(() -> new Menu("스테커3 버거", BigDecimal.valueOf(-1), savedMenuGroup))
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
