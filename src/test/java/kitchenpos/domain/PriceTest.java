package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("price 값이 음수이면 예외를 발생시킨다.")
    @Test
    void check_price_under_zero() {
        // given
        // when
        // then
        assertThatThrownBy(() -> Price.from(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("price 값이 null이면 예외를 발생시킨다.")
    @Test
    void check_price_isNull() {
        // given
        // when
        // then
        assertThatThrownBy(() -> Price.from(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
