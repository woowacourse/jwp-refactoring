package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PriceTest {

    @DisplayName("가격 객체를 생성한다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> new Price(BigDecimal.TEN));
    }

    @DisplayName("가격은 0원 미만일 경우 생성할 수 없다.")
    @Test
    void createException() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1000L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격과 수량을 곱한다.")
    @Test
    void multiply() {
        final Price price = new Price(BigDecimal.valueOf(1000));
        final long quantity = 2;

        final BigDecimal actual = price.multiply(quantity);

        assertThat(actual).isEqualTo(BigDecimal.valueOf(2000));
    }
}