package kitchenpos.domain;

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
    private Order order;

    private Long orderHistoryId;

    private Long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Long orderHistoryId, Long quantity) {
        this.order = order;
        this.orderHistoryId = orderHistoryId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getOrderHistoryId() {
        return orderHistoryId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
