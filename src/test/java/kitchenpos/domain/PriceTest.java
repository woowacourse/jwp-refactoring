package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void construct(final int value) {
        final var price = new Price(BigDecimal.valueOf(value));

    }

    @DisplayName("가격은 음수가 될 수 없다")
    @Test
    void constructWithNegativeValue() {
        final var value = BigDecimal.valueOf(-1);

        assertThatThrownBy(()-> new Price(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양수여야 합니다.");
    }

    @DisplayName("가격을 비교하다")
    @ParameterizedTest
    @CsvSource(value = {"100,50,1", "50,50,0", "50,100,-1"})
    void compareTo(final BigDecimal value1, final BigDecimal other, final int expected) {
        final var price = new Price(value1);

        assertThat(price.compareTo(other)).isEqualTo(expected);
    }
}
