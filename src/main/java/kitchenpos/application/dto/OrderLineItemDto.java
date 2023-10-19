package kitchenpos.application.dto;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemDto {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    public OrderLineItemDto(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto from(OrderLineItem orderLineItem) {
        Long seq = orderLineItem.getSeq();
        Long orderId = orderLineItem.getOrder().getId();
        Long menuId = orderLineItem.getMenu().getId();
        Long quantity = orderLineItem.getQuantity();
        return new OrderLineItemDto(seq, orderId, menuId, quantity);
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
