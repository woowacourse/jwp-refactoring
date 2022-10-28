package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem create(final Long menuId) {
        return new OrderLineItem(null, null, menuId, 0);
    }

    public static OrderLineItem create(final Long menuId, final Order order) {
        return new OrderLineItem(null, order, menuId, 0);
    }
}
