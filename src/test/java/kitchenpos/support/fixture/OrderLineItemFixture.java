package kitchenpos.support.fixture;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem create(final Long menuId) {
        return new OrderLineItem(menuId, 0);
    }
}
