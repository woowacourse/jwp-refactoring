package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.exception.NotConvertableStatusException;

public class Order {
    private final Long id;
    private final Long orderTableId;
    private OrderStatus orderStatus;
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
        this(id, orderTableId, orderStatus, orderedTime, null);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime, null);
    }

    public Order(Order order, List<OrderLineItem> orderLineItems) {
        this(order.id, order.orderTableId, order.orderStatus, order.orderedTime, orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new NotConvertableStatusException();
        }
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
