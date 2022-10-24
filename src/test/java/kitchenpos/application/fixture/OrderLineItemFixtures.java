package kitchenpos.application.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtures {

    public static final OrderLineItem generateOrderLineItem(final Long menuId, final long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
