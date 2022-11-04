package kitchenpos.domain.table;

import static kitchenpos.domain.common.OrderStatus.COMPLETION;
import static kitchenpos.domain.common.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.badrequest.CompletedOrderCannotChangeException;
import org.junit.jupiter.api.Test;

class OrderStatusRecordTest {

    @Test
    void 주문_상태가_완료인_주문_상태_기록은_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderStatusRecord orderStatusRecord = new OrderStatusRecord(1L, orderTable, COMPLETION);

        assertThatThrownBy(() -> orderStatusRecord.changeOrderStatus(MEAL.name()))
                .isInstanceOf(CompletedOrderCannotChangeException.class);
    }
}
