package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private LineItems orderLineItems;

    public Order() {
    }

    public Order(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new LineItems(orderLineItems);
    }

    public Order(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        this(null, orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new LineItems(orderLineItems);
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.lineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new LineItems(orderLineItems);
    }

    private static class LineItems {

        private final List<OrderLineItem> lineItems;

        private LineItems(final List<OrderLineItem> lineItems) {
            validateNotEmpty(lineItems);
            this.lineItems = lineItems;
        }

        private void validateNotEmpty(final List<OrderLineItem> lineItems) {
            if (lineItems.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
    }
}
