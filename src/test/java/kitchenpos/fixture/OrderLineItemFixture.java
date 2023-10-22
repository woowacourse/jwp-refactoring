package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderLineItemFixture {
    public static OrderLineItem ORDER_LINE_ITEM(Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

}
