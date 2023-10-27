package kitchenpos.Fixture;

import kitchenpos.menu.Menu;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;

public abstract class Fixture {

    public static OrderLineItem orderLineItemFixture(Order order, Menu menu, long quantity) {
        return new OrderLineItem(null, order, menu.getId(), quantity);
    }
}
