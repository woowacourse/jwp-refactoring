package kitchenpos.domain.table;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import kitchenpos.domain.OrderStatus;

@Entity
public class OrderRecord {

    @Id
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    protected OrderRecord() {
    }

    public OrderRecord(Long orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public boolean isCompleted() {
        return orderStatus.isCompleted();
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
