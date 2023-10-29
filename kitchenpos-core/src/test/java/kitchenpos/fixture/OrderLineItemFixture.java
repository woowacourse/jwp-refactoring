package kitchenpos.fixture;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem orderLineItem(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }
}
