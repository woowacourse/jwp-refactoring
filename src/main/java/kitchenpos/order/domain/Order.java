package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final OrderLineItems orderLineItems;

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, null);
    }

    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final OrderLineItems orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long orderTableId, final OrderLineItems orderLineItems) {
        this(null, orderTableId, null, null, orderLineItems);
    }

    public boolean hasValidSize(final long size) {
        return orderLineItems.getSize() == size;
    }

    public boolean hasStatus(final OrderStatus status) {
        return orderStatus.equals(status.name());
    }

    public Order updateOrderStatus(final OrderStatus orderStatus) {
        validateCompletion();
        return new Order(id, orderTableId, orderStatus.name(), orderedTime, orderLineItems);
    }

    private void validateCompletion() {
        if (hasStatus(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }
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
        return orderLineItems.getValue();
    }

    public List<Long> getMenuIds() {
        return orderLineItems.getMenuIds();
    }

    public Order setOrderLineItems(final OrderLineItems orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
