package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private long quantity;

    public OrderLineItem(final Order order, final Menu menu, final long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    protected OrderLineItem() {
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
