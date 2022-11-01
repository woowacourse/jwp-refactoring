package kitchenpos.ui.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private OrderLineItemRequest() {

    }

    public OrderLineItem toOrderLineItemRequest() {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }
}
