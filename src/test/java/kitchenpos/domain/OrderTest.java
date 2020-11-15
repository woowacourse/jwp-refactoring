package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        Order order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now());
        Order updatedOrder = order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문 상태가 '완료'라면 예외가 발생한다")
    void changeOrderStatusWithCompleteThrowException() {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now());

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}