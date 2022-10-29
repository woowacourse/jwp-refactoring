package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.INVALID_CHANGE_ORDER_STATUS_EXCEPTION;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 이미_완료된_주문의_상태를_변경_할_경우_예외를_반환한다() {
        final Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), null);
        Assertions.assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_CHANGE_ORDER_STATUS_EXCEPTION.getMessage());
    }
}
