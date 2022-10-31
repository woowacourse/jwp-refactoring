package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void changeStatus() {
        Order order = Order.of(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of());

        order.changeStatus(OrderStatus.MEAL.name());

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void changeStatusThrowExceptionWhenCompletionStatus() {
        Order order = Order.of(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), List.of());

        assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 이미 완료되었습니다.");
    }
}
