package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        order.addOrderLineItem(this);
        this.menu = menu;
        this.quantity = quantity;
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
