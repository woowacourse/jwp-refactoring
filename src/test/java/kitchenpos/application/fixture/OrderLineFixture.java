package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineFixture {

    public static OrderLineItem createWithOrderAndMenu(Menu menu, long count) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(count);

        return orderLineItem;
    }
}
