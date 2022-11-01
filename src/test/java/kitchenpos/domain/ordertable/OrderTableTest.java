package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTableTest {
    @Test
    void 손님_수는_음수가_될_수_없다() {
        assertThatThrownBy(() -> new OrderTable(null, -1, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹화된_테이블은_빈_상태로_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(1L, 2, false);

        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블은_손님_수를_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(null, 2, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수를_음수로_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(null, 2, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
