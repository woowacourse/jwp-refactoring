package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(Long orderTableId, LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        this.orderedTime = orderedTime;
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
