package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import kitchenpos.factory.OrderFactory;
import kitchenpos.factory.OrderLineItemFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        OrderLineItem orderLineItem = OrderLineItemFactory.builder()
            .seq(1L)
            .orderId(1L)
            .menuId(1L)
            .quantity(2L)
            .build();

        order = OrderFactory.builder()
            .id(1L)
            .orderTableId(1L)
            .orderStatus(OrderStatus.COOKING)
            .orderedTime(LocalDateTime.now())
            .orderLineItems(orderLineItem)
            .build();
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태 변경을 실패한다 - 이미 COMPLETION 상태인 경우")
    @Test
    void changeOrderStatusFail_whenOrderStatusInOrderIsCompletion() {
        // given
        order = OrderFactory.copy(order)
            .orderStatus(OrderStatus.COMPLETION)
            .build();

        // when
        ThrowingCallable throwingCallable = () -> order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문과 관련된 요리를 시작한다")
    @Test
    void startCooking() {
        // when
        order.startCooking();

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderedTime()).isNotNull();
    }
}
