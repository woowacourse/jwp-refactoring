package kitchenpos.support.fixtures;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixtures {

    public static OrderLineItem create(final Order order, final Long menuId, final long quantity) {
        return new OrderLineItem(null, order, menuId, quantity);
    }
}
