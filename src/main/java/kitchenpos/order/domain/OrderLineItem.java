package kitchenpos.order.domain;

import javax.persistence.Column;
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

    @ManyToOne
    private Order order;

    private Long orderMenuId;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Long orderMenuId, long quantity) {
        this.order = order;
        this.orderMenuId = orderMenuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
