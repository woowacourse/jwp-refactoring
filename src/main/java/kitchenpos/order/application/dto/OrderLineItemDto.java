package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final Long quantity;

    public OrderLineItemDto(final Long seq, final Long orderId, final Long menuId,
        final Long quantity) {
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

    public static OrderLineItemDto from(final OrderLineItem orderLineItem) {
        return new OrderLineItemDto(
            orderLineItem.getSeq(),
            orderLineItem.getOrder().getId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity()
        );
    }

    @Override
    public String toString() {
        return "OrderLineItemDto{" +
            "seq=" + seq +
            ", orderId=" + orderId +
            ", menuId=" + menuId +
            ", quantity=" + quantity +
            '}';
    }
}
