package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final long quantity;

    private OrderLineItemResponse(Long seq, Long orderId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }
}
