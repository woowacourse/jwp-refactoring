package kitchenpos.domain.common;

import kitchenpos.exception.badrequest.NegativeNumberOfGuestsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @Test
    void 인원_수는_음수일_수_없다() {
        Assertions.assertThatThrownBy(() -> new NumberOfGuests(-1)).isInstanceOf(NegativeNumberOfGuestsException.class);
    }
}
