package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.INVALID_CHANGE_ORDER_STATUS_EXCEPTION;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 이미_완료된_주문의_상태를_변경_할_경우_예외를_반환한다() {
        final Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(1L, 1L, 1L)));

        final Order changedOrder = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(1L, 1L, 1L)));

        Assertions.assertThatThrownBy(() -> order.changeOrderStatus(changedOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_CHANGE_ORDER_STATUS_EXCEPTION.getMessage());
    }
}
