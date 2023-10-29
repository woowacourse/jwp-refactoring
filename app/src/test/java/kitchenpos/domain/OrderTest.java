package kitchenpos.domain;

import kitchenpos.order.Order;
import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("조리중 상태의 주문은 상태를 변경할 수 있다.")
    @Test
    void changeStatus_success() {
        // given
        final OrderStatus beforeChangedOrderStatus = OrderStatus.COOKING;
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(1), false);
        final Order order = new Order(beforeChangedOrderStatus, orderTable);

        final OrderStatus afterChangedOrderStatus = OrderStatus.MEAL;

        // when
        order.changeStatus(afterChangedOrderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(afterChangedOrderStatus);
    }

    @DisplayName("계산 완료 상태의 주문은 상태를 변경할 수 없다.")
    @Test
    void changeStatus_fail_when_orderStatus_is_COMPLETION() {
        // given
        final OrderStatus beforeChangedOrderStatus = OrderStatus.COMPLETION;
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(1), false);
        final Order order = new Order(beforeChangedOrderStatus, orderTable);

        final OrderStatus afterChangedOrderStatus = OrderStatus.MEAL;

        // then
        assertThatThrownBy(() -> order.changeStatus(afterChangedOrderStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
