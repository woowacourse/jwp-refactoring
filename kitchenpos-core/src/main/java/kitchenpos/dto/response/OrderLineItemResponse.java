package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    private OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse toResponse(final OrderLineItem orderLineItem, final Long orderId) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderId,
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
