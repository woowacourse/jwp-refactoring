package kitchenpos.support.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem create(final Long menuId) {
        return new OrderLineItem(menuId, 0);
    }

    public static OrderLineItem create(final Long menuId, final Order order) {
        return new OrderLineItem(null, order, menuId, 0);
    }
}
