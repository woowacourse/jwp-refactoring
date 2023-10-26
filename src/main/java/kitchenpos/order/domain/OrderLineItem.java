package kitchenpos.order.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.vo.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private long menuId;

    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, long menuId, Quantity quantity) {
        this(null, order, menuId, quantity);
    }

    public OrderLineItem(Long seq, Order order, long menuId, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long seq() {
        return seq;
    }

    public Order order() {
        return order;
    }

    public Long menuId() {
        return menuId;
    }

    public Quantity quantity() {
        return quantity;
    }
}
