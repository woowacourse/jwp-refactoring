package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    private Long orderId;

    @OneToOne(fetch = LAZY)
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long orderId, final Menu menu, final long quantity) {
        this(null, orderId, menu, quantity);
    }

    public OrderLineItem(final Long seq, final Long orderId, final Menu menu, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void changeOrder(final Long order) {
        this.orderId = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
