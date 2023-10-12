package kitchenpos.supports;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    private Long seq = null;
    private Long orderId = 1L;
    private Long menuId = 2L;
    private long quantity = 3L;

    private OrderLineItemFixture() {
    }

    public static OrderLineItemFixture fixture() {
        return new OrderLineItemFixture();
    }

    public OrderLineItemFixture seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemFixture orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemFixture menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemFixture quantity(long quantity) {
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
