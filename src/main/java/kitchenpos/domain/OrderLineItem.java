package kitchenpos.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItem(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, quantity);
    }

    public static OrderLineItem of(final Long id, final Long orderId, final Long menuId, final long quantity) {
        return new OrderLineItem(id, orderId, menuId, quantity);
    }

    public static OrderLineItem of(final Long orderId, final Long menuId, final long quantity) {
        return new OrderLineItem(null, orderId, menuId, quantity);
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
