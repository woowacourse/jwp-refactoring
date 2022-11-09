package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long orderMenuId;
    private final long quantity;

    private OrderLineItemResponse(final Long seq, final Long orderMenuId, final long quantity) {
        this.seq = seq;
        this.orderMenuId = orderMenuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderMenu().getId(),
                orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
