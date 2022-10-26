package kitchenpos.domain;

import static kitchenpos.OrderFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.OrderTableFixtures;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void constructOrder() {
        // given
        long orderTableId = 1L;
        String orderStatus = OrderStatus.COOKING.name();
        LocalDateTime orderedTime = LocalDateTime.now();
        List<OrderLineItem> orderLineItems = List.of();
        // when
        Order order = new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
        // then
        assertThat(order).isNotNull();
    }

    @Test
    void changeOrderTable() {
        // given
        Order order = createOrder();
        long orderTableId = 10L;
        OrderTable orderTable = OrderTableFixtures.createOrderTable(orderTableId, null, 2, false);
        // when
        order.changeTable(orderTable);
        // then
        assertThat(order.getOrderTableId()).isEqualTo(orderTableId);
    }

    @Test
    void changeOrderTableWithEmptyOrderTable() {
        // given
        Order order = createOrder();
        OrderTable orderTable = OrderTableFixtures.createOrderTable();
        // when
        assertThatThrownBy(() -> order.changeTable(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatus() {
        // given
        Order order = createOrder();
        OrderStatus orderStatus = OrderStatus.MEAL;
        // when
        order.changeOrderStatus(orderStatus);
        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus.name());
    }

    @Test
    void changeOrderStatusWithAlreadyCompletionStatus() {
        // given
        Order order = createOrder(OrderStatus.COMPLETION.name());
        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalStateException.class);
    }
}
