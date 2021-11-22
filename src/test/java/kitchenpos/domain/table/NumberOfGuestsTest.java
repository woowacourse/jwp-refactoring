package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.exception.InvalidNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @DisplayName("손님의 수가 음수일 경우 - 실패")
    @Test
    void create() {
        //given
        //when
        //then
        assertThatThrownBy(() -> NumberOfGuests.create(-4))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }
}