package kitchenpos.support.fixture.domain;

import kitchenpos.order.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem getOrderLineItem(final Long orderId, final Long menuId, final long quantity) {
        return new OrderLineItem(null, orderId, menuId, quantity);
    }
}
