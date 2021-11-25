package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.exception.InvalidMenuQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuQuantity 단위 테스트")
class MenuQuantityTest {

    @DisplayName("MenuQuantity를 생성할 때")
    @Nested
    class Create {

        @DisplayName("value가 null 일 경우 예외가 발생한다.")
        @Test
        void nullException() {
            // when, then
            assertThatThrownBy(() -> new MenuQuantity(null))
                .isExactlyInstanceOf(InvalidMenuQuantityException.class);
        }

        @DisplayName("value가 0보다 작을 경우 예외가 발생한다.")
        @Test
        void negativeException() {
            // when, then
            assertThatThrownBy(() -> new MenuQuantity(-1L))
                .isExactlyInstanceOf(InvalidMenuQuantityException.class);
        }
    }

    @DisplayName("BigDecimal 값으로 곱셈 후 메뉴의 가격을 반환한다.")
    @Test
    void multiplyPrice() {
        // given
        MenuQuantity menuQuantity = new MenuQuantity(5L);

        // when
        MenuPrice menuPrice = menuQuantity.multiplyPrice(BigDecimal.valueOf(10));

        // then
        assertThat(menuPrice).isEqualTo(new MenuPrice(BigDecimal.valueOf(50)));
    }
}
