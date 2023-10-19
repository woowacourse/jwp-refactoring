package kitchenpos.order.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.order.exception.OrderIsCompletedException;
import kitchenpos.order.exception.OrderIsNotCompletedException;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTableId = orderTableId;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderIsNotCompleted();
        this.orderStatus = orderStatus;
    }

    private void validateOrderIsNotCompleted() {
        if (orderStatus.isCompleted()) {
            throw new OrderIsCompletedException();
        }
    }

    public void validateOrderIsCompleted() {
        if (orderStatus.isNotCompleted()) {
            throw new OrderIsNotCompletedException();
        }
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
