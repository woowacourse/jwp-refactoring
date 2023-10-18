package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public abstract class OrderLineItemFixture {

    private OrderLineItemFixture() {
    }

    public static OrderLineItem orderLineItem(final Order order, final Menu menu, final long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}
