package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order implements Entity {

    private Long id;
    private OrderTable orderTable;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(final Long id,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime) {
        this(id, null, orderTableId, orderStatus, orderedTime, null);
    }

    public Order(final Long id,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(id, null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long id,
                 final OrderTable orderTable,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        if (isNew()) {
            validateOnCreate();
        }
    }

    public void changeStatus(final OrderStatus orderStatus) {
        validateNotCompleted();
        this.orderStatus = orderStatus;
    }

    private void validateNotCompleted() {
        if (orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public void validateOnCreate() {
        validateOrderLineItemsNotEmpty();
        validateOrderTableNotEmpty();
    }

    private void validateOrderTableNotEmpty() {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemsNotEmpty() {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
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
