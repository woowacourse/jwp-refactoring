package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name = "orders")
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private Order(Builder builder) {
        this.orderTableId = builder.orderTableId;
        this.orderStatus = builder.orderStatus;
        this.orderedTime = builder.orderedTime;
        this.orderLineItems = builder.orderLineItems;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addOrderItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(final String orderStatus,
                                  final List<OrderLineItem> orderLineItems) {
        this.orderStatus = OrderStatus.valueOf(orderStatus).name();
        this.orderLineItems = orderLineItems;
    }

    public static class Builder {
        private Long id;
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTableId(final Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public Builder orderStatus(final String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderedTime(final LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(final List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
