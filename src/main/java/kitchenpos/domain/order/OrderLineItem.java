package kitchenpos.domain.order;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.menu.Menu;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @ManyToOne(optional = false)
    private Order order;

    @ManyToOne(optional = false)
    private Menu menu;

    @Embedded
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Menu menu, final Long quantity) {
        this(null, menu, quantity);
    }

    public OrderLineItem(final Order order, final Menu menu, final Long quantity) {
        this(null, order, menu, quantity);
    }

    public OrderLineItem(final Long seq, final Order order, final Menu menu, final Long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = new OrderLineItemQuantity(quantity);
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

    public Long getQuantity() {
        return quantity.getValue();
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
