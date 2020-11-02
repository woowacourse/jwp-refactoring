package kitchenpos.domain;

import kitchenpos.builder.OrderLineItemBuilder;

public class OrderLineItem {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public OrderLineItemBuilder toBuilder() {
        return new OrderLineItemBuilder(seq, orderId, menuId, quantity);
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
