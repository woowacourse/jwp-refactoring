package kitchenpos.domain.order;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.domain.ordertable.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void 주문을_생성할_수_있다() {
        OrderTable orderTable = new OrderTable(null, 2, false);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 2);

        assertDoesNotThrow(() -> new Order(orderTable, COOKING, List.of(orderLineItem)));
    }

    @Test
    void 빈_테이블에는_주문을_생성할_수_없다() {
        OrderTable orderTable = new OrderTable(null, 2, true);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 2);

        assertThatThrownBy(() -> new Order(orderTable, COOKING, List.of(orderLineItem)));
    }

    @Test
    void 주문_항목은_비어있을_수_없다() {
        OrderTable orderTable = new OrderTable(null, 2, false);

        assertThatThrownBy(() -> new Order(orderTable, COOKING, List.of()));
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderTable orderTable = new OrderTable(null, 2, false);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 2);

        Order order = new Order(orderTable, COOKING, List.of(orderLineItem));
        order.changeOrderStatus(MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(MEAL);
    }

    @Test
    void 완료_상태의_주문은_주문_상태를_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(null, 2, false);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 2);

        Order order = new Order(orderTable, COMPLETION, List.of(orderLineItem));

        assertThatThrownBy(() -> order.changeOrderStatus(MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
