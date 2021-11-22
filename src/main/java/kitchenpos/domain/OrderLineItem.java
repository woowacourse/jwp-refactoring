package kitchenpos.domain;

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

    private Long menuId;

    private long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, menuId, quantity, null);
    }

    public OrderLineItem(Long seq, Long menuId, long quantity, Orders orders) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.orders = orders;
    }

    public void belongsTo(Orders orders) {
        this.orders = orders;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Orders getOrders() {
        return orders;
    }
}
