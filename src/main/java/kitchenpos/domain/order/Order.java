package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new OrderLineItems(orderLineItems);
        validateOrderTable(orderTable);
        validateOrderLineItems(this.orderLineItems);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
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

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = OrderStatus.of(orderStatus);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getCollection();
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }
}
