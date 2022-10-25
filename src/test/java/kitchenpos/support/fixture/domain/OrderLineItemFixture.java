package kitchenpos.support.fixture.domain;

import kitchenpos.domain.OrderLineItem;

public enum OrderLineItemFixture {

    ONE(1L),
    TWO(2L)
    ;

    private final Long quantity;

    OrderLineItemFixture(Long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem getOrderLineItem(Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public OrderLineItem getOrderLineItem(Long menuId, Long orderId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public OrderLineItem getOrderLineItem(Long seq, Long orderId, Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
