package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("상태를 올바르게 변경한다")
    @Test
    void changeStatus() {
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order order = new Order(1L, 1L, orderStatus.name(), LocalDateTime.now());
        OrderStatus toChangeStatus = OrderStatus.COMPLETION;
        order.changeStatus(toChangeStatus);

        String actual = order.getOrderStatus();
        assertThat(actual).isEqualTo(toChangeStatus.name());
    }

    @DisplayName("Complete상태에서 상태 변경시 예외가 발생한다")
    @Test
    void changeStatusWithCompleteStatus() {
        OrderStatus orderStatus = OrderStatus.COMPLETION;
        Order order = new Order(1L, 1L, orderStatus.name(), LocalDateTime.now());

        OrderStatus toChangeStatus = OrderStatus.MEAL;
        assertThatThrownBy(() -> order.changeStatus(toChangeStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
