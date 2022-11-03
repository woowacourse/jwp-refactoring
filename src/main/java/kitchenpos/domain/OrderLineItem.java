package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private OrderMenu orderMenu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final OrderMenu orderMenu, final long quantity) {
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }
}
