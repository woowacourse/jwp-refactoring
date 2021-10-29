package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderTableId;
    @Column(nullable = false)
    private String orderStatus;
    @Column(nullable = false)
    private LocalDateTime orderedTime;
    @Transient
    private List<OrderLineItem> orderLineItems;

    public Order() {
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

    public static class Builder {
        private Long id;
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems = new ArrayList<>();

        private Builder() {
        }

        public Builder of(Order order) {
            this.id = order.id;
            this.orderTableId = order.orderTableId;
            this.orderStatus = order.orderStatus;
            this.orderedTime = order.orderedTime;
            this.orderLineItems = new ArrayList<>(order.orderLineItems);
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public Builder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = new ArrayList<>(orderLineItems);
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
