package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("가격이 null 이라면 예외가 발생한다")
    @Test
    void priceIsNull_throwsException() {
        assertThatThrownBy(
                () -> new Product("콜라", null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0 미만 이라면 예외가 발생한다")
    @Test
    void priceIsUnderZero_throwsException() {
        assertThatThrownBy(
                () -> new Product("콜라", new BigDecimal(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
