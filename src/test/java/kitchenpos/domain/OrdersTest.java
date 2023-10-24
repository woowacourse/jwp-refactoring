package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrdersTest {

    @DisplayName("진행 중인 주문이 있는지 확인한다.")
    @Test
    void hasProceedingOrder() {
        // given
        final Orders orders = new Orders(
            List.of(
                new Order(1L, OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 1L, null))),
                new Order(2L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, null))),
                new Order(3L, OrderStatus.MEAL, List.of(new OrderLineItem(1L, 1L, null)))
            )
        );

        // when
        final boolean hasProceedingOrder = orders.hasProceedingOrder();

        // then
        assertThat(hasProceedingOrder).isTrue();
    }
}
