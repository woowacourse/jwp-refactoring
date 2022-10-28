package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Order {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validateOrderTableId(orderTableId);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderTableId(Long orderTableId) {
        if (orderTableId == null) {
            throw new IllegalArgumentException();
        }
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime, null);
    }

    public Order changeOrderLineItems(List<OrderLineItem> orderLineItems) {
        return new Order(this.id, this.orderTableId, this.orderStatus, this.orderedTime, orderLineItems);
    }

    public boolean isCompletion() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public Order changeOrderStatus(OrderStatus orderStatus) {
        if (this.isCompletion()) {
            throw new IllegalArgumentException();
        }
        return new Order(this.id, this.orderTableId, orderStatus, this.orderedTime, this.orderLineItems);
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
