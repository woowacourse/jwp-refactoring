package kitchenpos.domain;

public class OrderLineItem {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long orderId, final Long menuId, final long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItem(final Long seq, final Long orderId, final Long menuId, final long quantity) {
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
