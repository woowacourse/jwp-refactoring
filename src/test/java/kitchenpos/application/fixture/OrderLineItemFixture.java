package kitchenpos.application.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem createOrderLineItem(final Long orderId, final Long menuId, final int quantity) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
