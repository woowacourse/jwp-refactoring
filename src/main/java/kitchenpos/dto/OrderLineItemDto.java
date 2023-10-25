package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemDto(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto toDto(final OrderLineItem orderLineItem, final Long orderId) {
        return new OrderLineItemDto(orderLineItem.getSeq(), orderId, orderLineItem.getMenuSnapShot().getMenuId(), orderLineItem.getQuantity());
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
