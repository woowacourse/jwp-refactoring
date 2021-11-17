package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(OrderBuilder orderBuilder) {
        this.id = orderBuilder.id;
        this.orderTableId = orderBuilder.orderTableId;
        this.orderStatus = orderBuilder.orderStatus;
        this.orderedTime = orderBuilder.orderedTime;
        this.orderLineItems = orderBuilder.orderLineItems;
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

    public static class OrderBuilder {

        private Long id;
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public OrderBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder setOrderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public OrderBuilder setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderBuilder setOrderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public OrderBuilder setOrderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
