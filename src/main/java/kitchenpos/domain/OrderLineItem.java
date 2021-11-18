package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    @JoinColumn(name = "order_id")
    @ManyToOne
    private Order order;
    @JoinColumn(name = "menu_id")
    @ManyToOne
    private Menu menu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this(null, menu, quantity);
    }

    public OrderLineItem(final Long seq, final Menu menu, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = new Quantity(quantity);
    }

    public void setOrder(final Order order) {
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

    public Quantity getQuantity() {
        return quantity;
    }
}
