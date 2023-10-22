package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @DisplayName("OrderStatus의 상태를 변경한다.")
    @Test
    void change_order_Status() {
        // given
        final Order order = new Order(1L);
        String changingStatus = OrderStatus.COMPLETION.name();

        // when
        order.changeStatus(changingStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(changingStatus);
    }

    @DisplayName("현재 OrderStatus의 상태가 Completion이면 예외를 발생한다.")
    @Test
    void change_status_fail() {
        // given
        final Order completionOrder = new Order(1L, 1L, OrderStatus.COMPLETION.name());
        String changingStatus = OrderStatus.COOKING.name();

        // when
        // then
        assertThatThrownBy(() -> completionOrder.changeStatus(changingStatus))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
