package kitchenpos.application.dto.response;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemDto {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final Integer quantity;

    public OrderLineItemDto(Long seq, Long orderId, Long menuId, Integer quantity) {
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

    public Integer getQuantity() {
        return quantity;
    }
}
