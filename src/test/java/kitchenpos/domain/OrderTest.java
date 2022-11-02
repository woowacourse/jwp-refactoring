package kitchenpos.domain;

import static kitchenpos.OrderFixtures.createOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.OrderTableFixtures;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void constructOrder() {
        // given
        List<OrderLineItem> orderLineItems = List.of();
        boolean isTableEmpty = false;
        // when
        Order order = Order.of(1L, orderLineItems, isTableEmpty);

        // then
        assertThat(order).isNotNull();
    }

    @Test
    void constructOrderWithEmptyOrderTable() {
        // given
        List<OrderLineItem> orderLineItems = List.of();
        boolean isTableEmpty = true;

        // when & then
        assertThatThrownBy(() -> Order.of(1L, orderLineItems, isTableEmpty))
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
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    @Test
    void changeOrderStatusWithAlreadyCompletionStatus() {
        // given
        Order order = createOrder(OrderStatus.COMPLETION);
        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isCompleted() {
        // given
        Order order = createOrder();
        // when
        boolean isCompleted = order.isCompleted();
        // then
        assertThat(isCompleted).isFalse();
    }
}
