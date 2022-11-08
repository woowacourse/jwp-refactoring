package kitchenpos.support.fixture;

import kitchenpos.order.OrderPrice;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem create(final String name, final Long price) {
        return new OrderLineItem(name, new OrderPrice(price), 0);
    }

    public static OrderLineItem create(final String name, final OrderPrice price) {
        return new OrderLineItem(name, price, 0);
    }
}
