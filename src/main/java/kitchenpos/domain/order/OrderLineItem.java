package kitchenpos.domain.order;

import kitchenpos.common.BaseDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    private Long menuId;
    private long quantity;

    public OrderLineItem(final Long seq, final Long menuId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, menuId, quantity);
    }

    protected OrderLineItem() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
