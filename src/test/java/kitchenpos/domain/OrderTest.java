package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void Order를_생성한다() {
        // given, when
        final Order order = new Order(1);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    void Order의_상태를_변경한다() {
        // given
        final Order order = new Order(1);

        // when
        order.changeStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void Order의_상태를_변경할_때_Order의_기존_상태가_COMPLETION이면_예외가_발생한다() {
        // given
        final Order order = new Order(1);
        order.changeStatus(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}