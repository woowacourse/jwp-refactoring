package kitchenpos.domain;

import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Nested;
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

    @Test
    void 테이블의_인원을_음수로_설정하면_예외가_발생한다() {
        //given
        int numberOfGuests = 1;
        boolean empty = true;
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //expect
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_empty일때_인원수를_변경할_수_없다() {
        //given
        int numberOfGuests = 1;
        boolean empty = true;
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //expect
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Nested
    class empty_설정 {

        @Test
        void 포함된_그룹이_있는_경우_예외가_발생한다() {
            //given
            OrderTable 테이블 = new OrderTable(1, false);
            테이블.setTableGroupId(1L);

            //expect
            assertThatThrownBy(() -> 테이블.changeEmpty(true))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
