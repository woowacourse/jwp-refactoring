package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(targetEntity = Orders.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Long quantity) {
        this(null, null, menu, quantity);
    }

    public OrderLineItem(Long seq, Orders orders, Menu menu, long quantity) {
        this.seq = seq;
        this.orders = orders;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Orders getOrders() {
        return orders;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public void combineWithOrder(Orders newOrder) {
        this.orders = newOrder;
    }

    public Long getOrderId() {
        return orders.getId();
    }
}
