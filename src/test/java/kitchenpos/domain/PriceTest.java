package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PriceTest {

    @DisplayName("Price 객체를 생성한다.")
    @Test
    void create() {
        // given
        BigDecimal price = BigDecimal.ZERO;

        // when, then
        assertDoesNotThrow(() -> new Price(price));
    }

    @DisplayName("값이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_LessThanMinimumValue_ExceptionThrown() {
        // given
        BigDecimal invalidValue = BigDecimal.valueOf(-1);

        // when, then
        assertThatThrownBy(() -> new Price(invalidValue))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
