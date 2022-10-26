package kitchenpos.fixtures;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtures {

    public static OrderLineItem create(final Long orderId, final Long menuId, final long quantity) {
        return new OrderLineItem(null, orderId, menuId, quantity);
    }
}
