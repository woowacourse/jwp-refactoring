package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemCreateResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemCreateResponse(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemCreateResponse of(final OrderLineItem orderLineItem) {
        return new OrderLineItemCreateResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
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
