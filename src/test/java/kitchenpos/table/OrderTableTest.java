package kitchenpos.table;

import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    void 테이블_그룹이_되어_있으면_비울_수_없다() {
        OrderTable orderTable = new OrderTable(3, false, true);

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void 주문_상태가_조리중_또는_식사중이면_비울_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable(3, false, false);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L);
        Order order = new Order(List.of(orderLineItem), 1L);
        order.changeOrderStatus(orderStatus);

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_비어있으면_손님_수를_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(3, true, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ValueSource(ints = {0, -1})
    @ParameterizedTest
    void 손님_수를_바꿀_때_양수이어야_한다(int numberOfGuests) {
        OrderTable orderTable = new OrderTable(3, false, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
