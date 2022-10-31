package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {

    @ParameterizedTest
    @ValueSource(longs = 1)
    void construct(final long value) {
        assert value > 0;

        final var quantity = new Quantity(value);
        assertThat(quantity.getValue()).isEqualByComparingTo(value);
    }

    @DisplayName("가격은 양수여야 한다")
    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void constructWithNegativeValue(final long value) {
        assert value <= 0;

        assertThatThrownBy(() -> new Quantity(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 양수여야 합니다.");
    }
}
