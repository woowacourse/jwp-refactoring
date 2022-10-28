package kitchenpos.fixtures;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtures {

    public static OrderLineItem create(final Order order, final Long menuId, final long quantity) {
        return new OrderLineItem(null, order, menuId, quantity);
    }
}
