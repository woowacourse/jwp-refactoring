package kitchenpos.order.domain;

import kitchenpos.BaseTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

class OrderTest extends BaseTest {

    @Test
    void 주문이_완료된_상태에서_주문_상태를_변경하면_예외를_던진다() {
        // given
        Order order = new Order(1L, Collections.emptyList(), LocalDateTime.now());
        order.completeOrder();

        // when, then
        Assertions.assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(OrderException.class);
    }
}
