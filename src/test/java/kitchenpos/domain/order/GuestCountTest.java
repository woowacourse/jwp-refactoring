package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GuestCountTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void construct(final int value) {
        assert value >= 0;

        final var numberOfGuests = new GuestCount(value);
        assertThat(numberOfGuests.getValue()).isEqualByComparingTo(value);
    }

    @DisplayName("손님 수는 음수가 될 수 없다")
    @ParameterizedTest
    @ValueSource(ints = -1)
    void constructWithNegativeValue(final int value) {
        assert value < 0;

        assertThatThrownBy(() -> new GuestCount(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수가 아니어야 합니다.");
    }
}
