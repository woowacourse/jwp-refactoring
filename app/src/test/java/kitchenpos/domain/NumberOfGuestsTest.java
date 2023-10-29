package kitchenpos.domain;

import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NumberOfGuestsTest {

    @DisplayName("인원 수는 0명 미만이 될 수 없다.")
    @Test
    void construct_fail_when_under_zero() {
        // given
        final int number = -10;

        // then
        assertThatThrownBy(() -> new NumberOfGuests(number))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
