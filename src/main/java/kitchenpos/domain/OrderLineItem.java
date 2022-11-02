package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Order order, final Long menuId, final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setOrder(final Order order) {
        this.order = order;
        order.addOrderLineItem(this);
    }
}
