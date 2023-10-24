package kitchenpos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    @Id
    private final Long id;
    private final OrderTable orderTable;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public Order updateOrderLineItems(List<OrderLineItem> savedOrderLineItems) {
        return new Order(id, orderTable, orderStatus, orderedTime, savedOrderLineItems);
    }

    public Order updateStatus(OrderStatus orderStatus) {
        return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static final class OrderBuilder {
        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        private OrderBuilder() {
        }

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder orderTable(OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public OrderBuilder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderBuilder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public OrderBuilder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
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
}
