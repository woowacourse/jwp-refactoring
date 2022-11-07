package kitchenpos.order.domain;

public class OrderLineItem {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long orderMenuId;
    private long quantity;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public void setOrderMenuId(final Long orderMenuId) {
        this.orderMenuId = orderMenuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }
}
