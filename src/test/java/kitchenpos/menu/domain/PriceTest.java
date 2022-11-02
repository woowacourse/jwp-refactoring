package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void construct(final long value) {
        assert value >= 0;

        final var price = makePrice(value);
        assertThat(price.getValue()).isEqualByComparingTo(BigDecimal.valueOf(value));
    }

    @DisplayName("가격은 음수가 될 수 없다")
    @ParameterizedTest
    @ValueSource(longs = -1)
    void constructWithNegativeValue(final long value) {
        assert value < 0;

        assertThatThrownBy(() -> makePrice(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 음수가 아니어야 합니다.");
    }

    @DisplayName("가격을 비교하다")
    @ParameterizedTest
    @CsvSource(value = {"100,50,1", "50,50,0", "50,100,-1"})
    void compareTo(final long value1, final long value2, final int expected) {
        final var price1 = makePrice(value1);
        final var price2 = makePrice(value2);
        assertThat(price1.compareTo(price2)).isEqualTo(expected);
    }

    private Price makePrice(final long value) {
        return new Price(BigDecimal.valueOf(value));
    }
}
