package kitchenpos.domain;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.orderException.IllegalOrderStatusException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTest {

    @Test
    void create() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, false);
        final Menu menu = new Menu("메뉴", null, null, null);
        final OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu, 1)));

        //when&then
        assertDoesNotThrow(() -> new Order(orderTable, orderLineItems));
    }

    @Test
    void changeOrderStatus() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, false);
        final Menu menu = new Menu("메뉴", null, null, null);
        final OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu, 1)));
        final Order order = new Order(orderTable, orderLineItems);

        //when&then
        assertDoesNotThrow(() -> order.changeOrderStatus(OrderStatus.COMPLETION));
    }

    @Test
    void validChangeOrderStatus() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, false);
        final Menu menu = new Menu("메뉴", null, null, null);
        final OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu, 1)));
        final Order order = new Order(orderTable, orderLineItems);

        order.changeOrderStatus(OrderStatus.COMPLETION);
        //when&then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessage("잘못된 주문 상태입니다.");
    }
}
