package kitchenpos.support.fixtures;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemFixtures {

    public static OrderLineItem create(final Order order, final OrderMenu orderMenu, final long quantity) {
        return new OrderLineItem(null, order, orderMenu, quantity);
    }

    public static OrderLineItem create(final Order order, final Menu menu, final long quantity) {
        final OrderMenu orderMenu = new OrderMenu(null, menu.getName(), menu.getPrice());
        return new OrderLineItem(null, order, orderMenu, quantity);
    }

    public static OrderLineItem toOrderLineItem(final Order order, final OrderMenu orderMenu, final long quantity) {
        return new OrderLineItem(null, order, orderMenu, quantity);
    }
}
