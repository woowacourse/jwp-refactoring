package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.domain.Quantity;
import kitchenpos.order.exception.InvalidQuantityException;
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
            assertThatThrownBy(() -> new Quantity(null))
                .isExactlyInstanceOf(InvalidQuantityException.class);
        }

        @DisplayName("value가 0보다 작을 경우 예외가 발생한다.")
        @Test
        void negativeException() {
            // when, then
            assertThatThrownBy(() -> new Quantity(-1L))
                .isExactlyInstanceOf(InvalidQuantityException.class);
        }
    }
}
