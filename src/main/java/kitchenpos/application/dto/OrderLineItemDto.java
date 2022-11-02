package kitchenpos.application.dto;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemDto {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    private OrderLineItemDto() {
    }

    public OrderLineItemDto(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
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

    public Long getQuantity() {
        return quantity;
    }
}
