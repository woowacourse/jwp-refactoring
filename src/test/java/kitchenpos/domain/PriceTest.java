package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("가격이 null일 경우 예외가 발생한다")
    @Test
    void validateNullPrice() {
        BigDecimal price = null;

        assertThatThrownBy(() -> new Price(price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수일 경우 예외가 발생한다")
    @Test
    void validateNegativePrice() {
        BigDecimal price = new BigDecimal(-1);

        assertThatThrownBy(() -> new Price(price))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
