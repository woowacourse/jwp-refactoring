package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.INVALID_CHANGE_ORDER_STATUS_EXCEPTION;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 이미_완료된_주문의_상태를_변경_할_경우_예외를_반환한다() {
        final Order order = new Order(1L, new OrderTable(5,true), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(1L, new OrderMenu("주문 테스트", BigDecimal.TEN), 1L)));

        Assertions.assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_CHANGE_ORDER_STATUS_EXCEPTION.getMessage());
    }
}
