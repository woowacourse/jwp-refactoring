package kitchenpos.order.domain;

import kitchenpos.BaseTest;
import kitchenpos.order.exception.OrderException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class OrderTest extends BaseTest {

    @Test
    void 주문이_완료된_상태에서_주문_상태를_변경하면_예외를_던진다() {
        // given
        Order order = new Order(LocalDateTime.now());
        order.completeOrder();

        // when, then
        Assertions.assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(OrderException.class);
    }
}
