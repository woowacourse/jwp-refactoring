package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("Price 객체를 생성한다.")
    @Test
    void create() {
        // given
        BigDecimal value = BigDecimal.ZERO;

        // when
        Price price = new Price(value);

        // then
        assertThat(price.getValue()).isZero();
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
