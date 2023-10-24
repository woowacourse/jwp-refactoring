package kitchenpos.order.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.order.domain.type.OrderStatus.COMPLETION;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.InvalidOrderStateException;
import kitchenpos.order.domain.type.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orderId", fetch = LAZY, cascade = ALL)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public void validateStatus() {
        if (!orderStatus.equals(COMPLETION)) {
            throw new InvalidOrderStateException("조리 중이거나 식사 중인 테이블의 상태는 변경 할 수 없습니다.");
        }
    }

    private Order(Builder builder) {
        this.id = builder.id;
        this.orderTableId = builder.orderTableId;
        this.orderStatus = builder.orderStatus;
        this.orderedTime = builder.orderedTime;
        this.orderLineItems = builder.orderLineItems;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTable() {
        return orderTableId;
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

        public Builder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
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
            return new Order(this);
        }
    }
}
