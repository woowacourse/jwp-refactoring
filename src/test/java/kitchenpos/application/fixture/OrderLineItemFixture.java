package kitchenpos.application.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public abstract class OrderLineItemFixture {

    private OrderLineItemFixture() {
    }

    public static OrderLineItem orderLineItem(final Long orderId, final Menu menu, final long quantity) {
        return new OrderLineItem(orderId, menu, quantity);
    }
}
