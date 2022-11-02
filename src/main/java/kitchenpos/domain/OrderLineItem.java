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

    private Long orderId;

    private Long orderHistoryId;

    private Long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long orderId, Long orderHistoryId, Long quantity) {
        this.orderId = orderId;
        this.orderHistoryId = orderHistoryId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderHistoryId() {
        return orderHistoryId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
