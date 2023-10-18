package kitchenpos.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long id, Orders orders, Menu menu, long quantity) {
        this.id = id;
        this.orders = orders;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Orders getOrder() {
        return orders;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

}
