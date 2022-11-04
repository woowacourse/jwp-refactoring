package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.badrequest.NegativeNumberOfGuestsException;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @Test
    void 인원_수는_음수일_수_없다() {
        assertThatThrownBy(() -> new NumberOfGuests(-1)).isInstanceOf(NegativeNumberOfGuestsException.class);
    }
}
