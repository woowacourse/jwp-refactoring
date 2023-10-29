package kitchenpos.order.domain;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import kitchenpos.common.exception.CustomException;
import kitchenpos.common.exception.ExceptionType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(value = STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(orphanRemoval = true, cascade = {PERSIST, MERGE, REMOVE})
    @JoinColumn(name = "orderId", nullable = false, updatable = false)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(
        Long id,
        OrderStatus orderStatus,
        Long orderTableId,
        LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems
    ) {

        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderStatus(Order other) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new CustomException(ExceptionType.ALREADY_COMPLETION_ORDER);
        }
        this.orderStatus = other.getOrderStatus();
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public Long geOrderTableId() {
        return orderTableId;
    }

    public static class Builder {

        private Long id;
        private Long orderTableId;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(id, orderStatus, orderTableId, orderedTime, orderLineItems);
        }
    }
}
