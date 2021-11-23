package kitchenpos.application.common.factory;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFactory {

    private OrderLineItemFactory() {
    }

    public static OrderLineItem create(Menu menu, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
