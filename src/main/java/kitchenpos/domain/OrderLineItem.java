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

    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long quantity, Menu menu) {
        this(null, quantity, null, menu);
    }

    public OrderLineItem(Long seq, Long quantity, Orders orders, Menu menu) {
        this.seq = seq;
        this.quantity = quantity;
        this.orders = orders;
        this.menu = menu;
    }

    public void belongsTo(Orders orders) {
        this.orders = orders;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Orders getOrders() {
        return orders;
    }

    public Menu getMenu() {
        return menu;
    }
}
