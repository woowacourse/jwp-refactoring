package kitchenpos.domain.fixture;

import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderMenu;

public abstract class OrderLineItemFixture {

    private OrderLineItemFixture() {
    }

    public static OrderLineItem orderLineItem(final Long orderId, final OrderMenu orderMenu, final long quantity) {
        return new OrderLineItem(orderId, orderMenu, quantity);
    }
}
