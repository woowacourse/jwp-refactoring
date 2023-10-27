package kitchenpos.order.application;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public static OrderLineItemDto from(final Long orderId, final OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getSeq(), orderId, orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
    }

    public OrderLineItemDto(final Long seq, final Long orderId, final Long menuId, final long quantity) {
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

    public long getQuantity() {
        return quantity;
    }

}
