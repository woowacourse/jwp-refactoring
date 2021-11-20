package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Quantity 단위 테스트")
class QuantityTest {

    @DisplayName("Quantity를 생성할 때")
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
