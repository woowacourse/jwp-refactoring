package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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

    public Long getQuantity() {
        return quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getSeq(),
            orderLineItem.getOrderId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity()
        );
    }
}
