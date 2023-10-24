package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    @DisplayName("가격이 null이면 예외가 발생한다.")
    void price_nullPrice() {
        // given
        final BigDecimal invalidPriceValue = null;

        // when & then
        assertThatThrownBy(() -> new Price(invalidPriceValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("가격이 음수이면 예외가 발생한다.")
    void price_negativePrice() {
        // given
        final BigDecimal invalidPriceValue = BigDecimal.valueOf(-1);

        // when & then
        assertThatThrownBy(() -> new Price(invalidPriceValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }
}
