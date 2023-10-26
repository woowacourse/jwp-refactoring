package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Embedded
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, OrderLineItemQuantity quantity) {
        this(null, menu, quantity);
    }

    public OrderLineItem(Long seq, Menu menu, OrderLineItemQuantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }
}
