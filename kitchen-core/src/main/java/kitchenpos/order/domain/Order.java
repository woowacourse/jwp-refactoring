package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import java.time.LocalDateTime;

public class Order {

    private final Long id;
    private final Long orderTableId;
    private OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final OrderLineItems orderLineItems;

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                 final LocalDateTime orderedTime, final OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final OrderLineItems orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long orderTableId, final OrderLineItems orderLineItems) {
        this(null, orderTableId, null, null, orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                 final LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, new OrderLineItems());
    }

    public static Order of(final OrderTable orderTable, final OrderLineItems orderLineItems) {
        validateOrderLineItemsNotEmpty(orderLineItems);
        validateOrderTableNotEmpty(orderTable);
        return new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    private static void validateOrderLineItemsNotEmpty(final OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void updateOrderStatus(final OrderStatus orderStatus) {
        validateNotCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateNotCompletion() {
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new IllegalArgumentException();
        }
    }
}
