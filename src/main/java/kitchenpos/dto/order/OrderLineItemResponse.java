package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;

    private Long orderId;

    private Long menuId;

    private long quantity;

    public OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
            orderLineItem.getOrder().getId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity());
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
