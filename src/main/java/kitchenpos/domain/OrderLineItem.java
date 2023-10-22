package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this(null, null, menu, quantity);
    }

    public OrderLineItem(final Long seq, final Order order, final Menu menu, final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void updateOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
