package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    void 테이블의_인원이_음수일_경우_예외가_발생한다() {
        //given
        int numberOfGuests = -1;
        boolean empty = true;

        //expect
        assertThatThrownBy(() -> new OrderTable(numberOfGuests, empty))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
