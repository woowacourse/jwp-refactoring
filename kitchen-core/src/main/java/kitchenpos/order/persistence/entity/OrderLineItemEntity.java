package kitchenpos.order.persistence.entity;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemEntity {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;


    public OrderLineItemEntity(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemEntity of(final Long orderId, final OrderLineItem orderLineItem) {
        return new OrderLineItemEntity(orderLineItem.getSeq(), orderId,
                orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(seq, menuId, quantity);
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
