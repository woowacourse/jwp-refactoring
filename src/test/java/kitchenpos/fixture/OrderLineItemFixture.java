package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    private static final OrderLineItem orderLineItem = new OrderLineItem(0L, 0L, 0L, 0L);

    public static OrderLineItem orderLineItem() {
        return orderLineItem;
    }
}
