package kitchenpos.test.fixtures;

import kitchenpos.order.domain.OrderLineItem;

public enum OrderLineItemFixtures {
    EMPTY(0L, 0L, 0L, 0L),
    BASIC(1L, 1L, 1L, 1L);

    private final long seq;
    private final long orderId;
    private final long menuId;
    private final long quantity;

    OrderLineItemFixtures(final long seq, final long orderId, final long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem get() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrder(orderId);
        orderLineItem.setMenu(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
