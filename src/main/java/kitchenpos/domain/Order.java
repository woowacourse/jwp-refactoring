package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, List.of());
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime, List.of());
    }

    public boolean isCompleted() {
        return orderStatus.isCompletion();
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
