package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @NotNull
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateChangeOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateChangeOrderStatus() {
        if (OrderStatus.COMPLETION == this.orderStatus) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
