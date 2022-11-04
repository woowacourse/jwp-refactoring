package kitchenpos.order.ui;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemRequest(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItemRequest() {
        return OrderLineItem.of(seq, orderId, menuId, quantity);
    }
}
