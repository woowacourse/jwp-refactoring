package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem orderLineItem(Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
