package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static OrderLineItem createOrderLineItem(
        final Long menuId,
        final Long quantity
    ) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenuId(menuId);
        return orderLineItem;
    }
}
