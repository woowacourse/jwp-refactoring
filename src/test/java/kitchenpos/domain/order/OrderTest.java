package kitchenpos.domain.order;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void 주문을_생성할_수_있다() {
        OrderLineItem orderLineItem = new OrderLineItem("메뉴", new BigDecimal(10000), 2);

        assertDoesNotThrow(() -> new Order(1L, COOKING, List.of(orderLineItem)));
    }

    @Test
    void 주문_항목은_비어있을_수_없다() {
        assertThatThrownBy(() -> new Order(1L, COOKING, List.of()));
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderLineItem orderLineItem = new OrderLineItem("메뉴", new BigDecimal(10000), 2);

        Order order = new Order(1L, COOKING, List.of(orderLineItem));
        order.changeOrderStatus(MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(MEAL);
    }

    @Test
    void 완료_상태의_주문은_주문_상태를_변경할_수_없다() {
        OrderLineItem orderLineItem = new OrderLineItem("메뉴", new BigDecimal(10000), 2);

        Order order = new Order(1L, COMPLETION, List.of(orderLineItem));

        assertThatThrownBy(() -> order.changeOrderStatus(MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
