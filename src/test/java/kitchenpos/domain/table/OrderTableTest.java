package kitchenpos.domain.table;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    void 테이블_그룹이_되어_있으면_비울_수_없다() {
        OrderTable orderTable = new OrderTable(1L, 3, false);
        orderTable.setTableGroupId(1L);

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void 주문_상태가_조리중_또는_식사중이면_비울_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable(1L, 3, false);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        Order order = new Order(1L, orderTable, List.of(orderLineItem));
        order.changeOrderStatus(orderStatus);
        orderTable.setOrders(new Orders(List.of(order)));

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
