package kitchenpos.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class OrderLineItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    protected OrderLineItem() { }

    public OrderLineItem(Long id, Long seq, Order order, Menu menu, long quantity) {
        this.id = id;
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this(null, seq, order, menu, quantity);
    }

    public Long getId() {
        return id;
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
