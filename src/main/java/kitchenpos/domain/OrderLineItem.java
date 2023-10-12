package kitchenpos.domain;

import java.util.Objects;

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

    public OrderLineItem(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

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

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLineItem)) return false;
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(orderId, that.orderId) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, orderId, menuId, quantity);
    }
}
