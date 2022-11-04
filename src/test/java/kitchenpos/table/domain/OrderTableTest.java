package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 손님수_변경_시_0미만인_경우_예외가_발생한다() {
        OrderTable orderTable = new OrderTable(1L, 3, true);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 0미만이 될 수 없습니다.");
    }

    @Test
    void 손님수_변경_시_빈테이블인_경우_예외가_발생한다() {
        OrderTable orderTable = new OrderTable(1L, 3, true);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 손님 수를 수정할 수 없습니다.");
    }
}
