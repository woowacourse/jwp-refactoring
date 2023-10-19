package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public final class OrderLineItemFixture {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemFixture() {
    }

    public static OrderLineItemFixture builder() {
        return new OrderLineItemFixture();
    }

    public OrderLineItemFixture withSeq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemFixture withOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemFixture withMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemFixture withQuantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}