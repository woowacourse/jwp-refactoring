package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class OrderLineItem {

    private Long seq;

    @Transient
    private Long orderId;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
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
}
