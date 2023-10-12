package kitchenpos.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long orderId, final Long menuId, final long quantity) {
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

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
