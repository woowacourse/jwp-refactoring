package kitchenpos.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

import static kitchenpos.fixture.MenuFixture.createMenuWithId;
import static kitchenpos.fixture.OrderFixture.createOrderWithId;

public class OrderLineItemFixture {

    private static OrderLineItem createOrderLineItem(Long seq, Order order, Menu menu, Long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public static OrderLineItem createOrderLineItemWithoutId(Order order, Menu menu, Long quantity) {
        return createOrderLineItem(null, order, menu, quantity);
    }

    public static OrderLineItem createOrderLineItemWithId(Long id) {
        return createOrderLineItem(id, createOrderWithId(2L), createMenuWithId(2L), 3L);
    }

    public static OrderLineItem createOrderLineItemWithOrderAndMenu(Order order, Menu menu) {
        return createOrderLineItem(null, order, menu, 1L);
    }

}
