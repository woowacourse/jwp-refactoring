package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private OrderedMenu orderedMenu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, OrderedMenu orderedMenu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.orderedMenu = orderedMenu;
        this.quantity = quantity;
    }

    public OrderLineItem(Order order, OrderedMenu orderedMenu, long quantity) {
        this(null, order, orderedMenu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderedMenu getOrderedMenu() {
        return orderedMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
