package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this(null, order, menu, quantity);
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        setOrder(order);
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    private void setOrder(Order order) {
        if (this.order != null) {
            this.order.getOrderLineItems().remove(this);
        }
        this.order = order;
        order.getOrderLineItems().add(this);
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }
}
