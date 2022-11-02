package kitchenpos.order.application.response;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getId();
        this.orderId = orderLineItem.getOrder().getId();
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
