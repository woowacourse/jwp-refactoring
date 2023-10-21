package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {
    @Test
    @DisplayName("변경하려는 손님 수는 음수일 수 없다.")
    void invalidGuestNumber() {
        assertThatThrownBy(
                () -> new NumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
