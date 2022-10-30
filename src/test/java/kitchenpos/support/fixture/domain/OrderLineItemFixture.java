package kitchenpos.support.fixture.domain;

import kitchenpos.order.domain.OrderLineItem;

public enum OrderLineItemFixture {

    ONE(1L),
    TWO(2L)
    ;

    private final Long quantity;

    OrderLineItemFixture(Long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem getOrderLineItem(Long menuId, Long orderId) {
        return new OrderLineItem(orderId, menuId, quantity);
    }

    public OrderLineItem getOrderLineItem(Long id, Long orderId, Long menuId) {
        return new OrderLineItem(id, orderId, menuId, quantity);
    }
}
