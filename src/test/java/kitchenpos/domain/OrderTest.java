package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
    @DisplayName("주문을 발생시킨다.")
    @Test
    void place() {
        Order actual = Order.place(1L, Collections.singletonList(new OrderLineItem(1L, 1L)));
        assertAll(
            () -> assertThat(actual).extracting(Order::getOrderTableId).isEqualTo(1L),
            () -> assertThat(actual).extracting(Order::getOrderStatus).isEqualTo(OrderStatus.COOKING),
            () -> assertThat(actual).extracting(Order::getOrderedTime).isNotNull(),
            () -> assertThat(actual).extracting(Order::getOrderLineItems,
                InstanceOfAssertFactories.list(OrderLineItem.class))
                .extracting(OrderLineItem::getOrder)
                .containsOnly(actual)
        );
    }

    @DisplayName("빈 주문 항목이 오면 예외 처리한다.")
    @Test
    void placeWithEmptyOrderLineItems() {
        assertThatThrownBy(() -> Order.place(1L, Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        Order order = Order.place(1L, Collections.singletonList(new OrderLineItem(1L, 1L)));
        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order).extracting(Order::getOrderStatus).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("현재 완료된 주문 상태를 변경하면 예외 처리한다.")
    @Test
    void changeOrderStatusWithCompletion() {
        Order order = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now(),
            Collections.singletonList(new OrderLineItem(1L, 1L)));

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
            .isInstanceOf(IllegalArgumentException.class);
    }
}