package kitchenpos.application.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

public abstract class OrderLineItemFixture {

    private OrderLineItemFixture() {
    }

    public static OrderLineItem orderLineItem(final Order order, final Menu menu, final long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}
