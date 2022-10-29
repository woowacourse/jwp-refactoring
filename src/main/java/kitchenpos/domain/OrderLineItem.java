package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, Long menuId, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, null, menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
