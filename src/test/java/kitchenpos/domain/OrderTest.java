package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void Order를_생성한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when
        final Order order = new Order(orderTable);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    void Order_생성_시_등록한_OrderTable이_빈_상태라면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        // when, then
        assertThatThrownBy(() -> new Order(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Order의_상태를_변경한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);
        final Order order = new Order(orderTable);

        // when
        order.changeStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void Order의_상태를_변경할_때_Order의_기존_상태가_COMPLETION이면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);
        final Order order = new Order(orderTable);
        order.changeStatus(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}