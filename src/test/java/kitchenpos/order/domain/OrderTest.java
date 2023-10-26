package kitchenpos.order.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void changeStatus() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(), new OrderLineItem()));

        // when
        order.changeStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }


    @Test
    void changeStatus_completionException() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(), new OrderLineItem()));
        order.changeStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
