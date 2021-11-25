package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.exception.InvalidNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NumberOfGuests 단위 테스트")
class NumberOfGuestsTest {

    @DisplayName("value가 0보다 작다면 예외가 발생한다.")
    @Test
    void negativeException() {
        // when, then
        assertThatThrownBy(() -> new NumberOfGuests(-1))
            .isExactlyInstanceOf(InvalidNumberOfGuestsException.class);
    }
}
