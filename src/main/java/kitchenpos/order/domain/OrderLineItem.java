package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private OrderedMenu orderedMenu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(OrderedMenu orderedMenu, long quantity) {
        this.orderedMenu = orderedMenu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public OrderedMenu getOrderedMenu() {
        return orderedMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
