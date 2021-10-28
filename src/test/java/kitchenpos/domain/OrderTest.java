package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문 생성 시 orderLineItem은 비어있을 수 없다")
    @Test
    void orderLineItemCantBeNull() {
        assertThatThrownBy(() ->
                new Order.Builder()
                        .orderLineItems(null)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 빌더패턴을 사용할 수 있다")
    @Test
    void createOrderWithBuilder() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);

        final Order order = new Order.Builder()
                .id(1L)
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.MAX)
                .orderLineItems(Arrays.asList(new OrderLineItem(), new OrderLineItem()))
                .build();

        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getOrderTableId()).isEqualTo(2L);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderedTime()).isEqualTo(LocalDateTime.MAX);
        assertThat(order.getOrderLineItems()).hasSize(2);
    }

    @DisplayName("주문의 OrderStatus가 Complete라면 변경할 수 없다")
    @Test
    void orderStatusCannotChangeToComplete() {
        final Order order = new Order.Builder()
                .orderStatus(OrderStatus.COMPLETION)
                .build();

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}