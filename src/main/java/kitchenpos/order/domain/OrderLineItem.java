package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private OrderMenu orderMenu;

    private long quantity;

    private OrderLineItem() {
    }

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
