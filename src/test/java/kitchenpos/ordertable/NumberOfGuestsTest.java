package kitchenpos.ordertable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {
    @DisplayName("손님 수가 음수이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-10, -1})
    void validateNumberOfGuests(int value) {
        assertThatThrownBy(() -> new NumberOfGuests(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 0보다 작을 수 없습니다.");
    }
}
