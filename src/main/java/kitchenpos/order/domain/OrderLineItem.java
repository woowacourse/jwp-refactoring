package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Order order, final Long menuId, final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Order order, final Long menuId, final long quantity) {
        this(null, order, menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
