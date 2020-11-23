package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    protected OrderLineItemCreateResponse() {
    }

    public OrderLineItemCreateResponse(OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.orderId = orderLineItem.getOrderId();
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

    public Long getQuantity() {
        return quantity;
    }
}
