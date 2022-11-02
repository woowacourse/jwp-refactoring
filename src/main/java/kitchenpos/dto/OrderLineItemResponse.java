package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long orderId, final OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.orderId = orderId;
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
