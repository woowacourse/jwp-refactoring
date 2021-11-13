package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Price 단위 테스트")
class PriceTest {

    @DisplayName("Price를 생성할 때")
    @Nested
    class Create {

        @DisplayName("value가 Null일 경우 예외가 발생한다.")
        @Test
        void valueNullException() {
            // when, then
            assertThatThrownBy(() -> new Price(null))
                .isExactlyInstanceOf(InvalidPriceException.class);
        }

        @DisplayName("value가 0보다 작을 경우 예외가 발생한다.")
        @Test
        void valueNegativeException() {
            // when, then
            assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isExactlyInstanceOf(InvalidPriceException.class);
        }
    }
}
