package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void 주문_테이블이_비어있으면_주문할_수_없다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, true);
        List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(1L, 1L, 1)
        );
        // when & then
        assertThatThrownBy(() -> new Order(null, orderTable, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목이_비어있으면_주문할_수_없다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, false);
        List<OrderLineItem> orderLineItems = List.of();
        // when & then
        assertThatThrownBy(() -> new Order(null, orderTable, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_완료면_주문_상태를_변경할_수_없다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, false);
        List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(1L, 1L, 1)
        );
        Order order = new Order(null, orderTable, orderLineItems);
        order.changeOrderStatus(OrderStatus.COMPLETION);
        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void 주문_상태가_완료가_아니면_주문_상태를_변경할_수_있다(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, false);
        List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(1L, 1L, 1)
        );
        Order order = new Order(null, orderTable, orderLineItems);
        order.changeOrderStatus(orderStatus);
        // when & then
        assertThatCode(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
                .doesNotThrowAnyException();
    }
}
