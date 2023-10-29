package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {

    @Test
    @DisplayName("손님 수는 음수가 될 수 없다")
    void numberOfGuests_negative() {
        // given
        final int negativeNumber = -1;

        // when & then
        assertThatThrownBy(() -> new NumberOfGuests(negativeNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수일 수 없습니다.");
    }
}
