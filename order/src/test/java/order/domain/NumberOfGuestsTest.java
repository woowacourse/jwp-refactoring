package order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @DisplayName("손님 수가 음수인 경우 예외가 발생한다.")
    @Test
    void create_failNegativeValue() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new NumberOfGuests(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
