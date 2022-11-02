package kitchenpos.order.application.response;

import java.io.Serializable;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse implements Serializable {

    private final Long seq;
    private final Long orderId;
    private final Long menuOrderId;
    private final long quantity;

    public OrderLineItemResponse(Long seq, Long orderId, Long menuOrderId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuOrderId = menuOrderId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
            orderLineItem.getMenuOrderId(),
            orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuOrderId() {
        return menuOrderId;
    }

    public long getQuantity() {
        return quantity;
    }
}
