package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem orderLineItem() {
        return new OrderLineItem(0L, 0L, 0L, 0L);
    }
}
