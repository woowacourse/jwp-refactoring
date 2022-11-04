package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {

    @DisplayName("가격이 음수인 경우에 예외가 발생한다.")
    @Test
    void new_ifPriceIsNegative_throwsException() {
        // when, then
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양수여야합니다.");
    }

    @DisplayName("가격이 null인 경우 예외가 발생한다.")
    @Test
    void new_ifPriceIsNull_throwsException() {
        // when, then
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양수여야합니다.");
    }
}
