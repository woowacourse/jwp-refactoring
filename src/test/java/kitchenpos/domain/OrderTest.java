package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.exception.IllegalOrderStatusException;
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
        final OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu.getId(), 1)));

        //when&then
        assertDoesNotThrow(() -> new Order(orderTable.getId(), orderLineItems));
    }

    @Test
    void changeOrderStatus() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, false);
        final Menu menu = new Menu("메뉴", null, null, null);
        final OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu.getId(), 1)));
        final Order order = new Order(orderTable.getId(), orderLineItems);

        //when&then
        assertDoesNotThrow(() -> order.changeOrderStatus(OrderStatus.COMPLETION));
    }

    @Test
    void validChangeOrderStatus() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, false);
        final Menu menu = new Menu("메뉴", null, null, null);
        final OrderLineItems orderLineItems = new OrderLineItems(List.of(new OrderLineItem(menu.getId(), 1)));
        final Order order = new Order(orderTable.getId(), orderLineItems);

        order.changeOrderStatus(OrderStatus.COMPLETION);
        //when&then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessage("잘못된 주문 상태입니다.");
    }
}
