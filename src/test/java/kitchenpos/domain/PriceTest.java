package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("가격이 null이 될 수 없다")
    @Test
    void priceIsNull_throwsException() {
        assertThatThrownBy(
                () -> new Price(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 0 미만이 될 수 없다")
    @Test
    void priceIsUnderZero_throwsException() {
        assertThatThrownBy(
                () -> new Price(new BigDecimal(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
