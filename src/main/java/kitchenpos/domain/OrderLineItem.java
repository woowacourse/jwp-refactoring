package kitchenpos.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@AttributeOverride(name = "id", column = @Column(name = "ORDER_LINE_ITEM_ID"))
@Entity
public class OrderLineItem extends BaseEntity {
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orderz order;
    private Long menuId;
    private long quantity;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
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

    public void setOrder(Orderz order) {
        this.order = order;
    }
}
