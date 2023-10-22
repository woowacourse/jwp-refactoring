package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem ORDER_LINE_ITEM(Menu menu, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem(null,
                menu,
                quantity);
        return orderLineItem;
    }
    
}
