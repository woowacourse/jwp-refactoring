package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.IllegalPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuPriceTest {

    @Test
    @DisplayName("가격은 null일 수 없다")
    void validateNullValue() {
        assertThatThrownBy(() -> new MenuPrice(null))
                .isExactlyInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("가격은 0원 이하일 수 없다")
    void validateNegativeValue() {
        assertThatThrownBy(() -> new MenuPrice(new BigDecimal(-1)))
                .isExactlyInstanceOf(IllegalPriceException.class);
    }
}
