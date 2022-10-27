package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
