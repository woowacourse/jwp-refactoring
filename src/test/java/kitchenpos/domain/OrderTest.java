package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.INVALID_CHANGE_ORDER_STATUS_EXCEPTION;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 이미_완료된_주문의_상태를_변경_할_경우_예외를_반환한다() {
        final Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(new OrderLineItem(1L,1L,1L,1L)));
        Assertions.assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_CHANGE_ORDER_STATUS_EXCEPTION.getMessage());
    }
}
