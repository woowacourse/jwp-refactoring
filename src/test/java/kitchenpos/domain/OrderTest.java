package kitchenpos.domain;

import static kitchenpos.OrderFixtures.createOrder;
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
        OrderStatus orderStatus = OrderStatus.COOKING;
        LocalDateTime orderedTime = LocalDateTime.now();
        List<OrderLineItem> orderLineItems = List.of();
        // when
        Order order = new Order(
                OrderTableFixtures.createOrderTable(1L, null, 2, false),
                orderStatus,
                orderedTime,
                orderLineItems
        );
        // then
        assertThat(order).isNotNull();
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
        Order order = createOrder(OrderStatus.COMPLETION);
        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalStateException.class);
    }
}
