package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.MenuSnapshot;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem ORDER_LINE_ITEM(Menu menu, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem(null,
                MenuSnapshot.from(menu),
                quantity);
        return orderLineItem;
    }
    
}
