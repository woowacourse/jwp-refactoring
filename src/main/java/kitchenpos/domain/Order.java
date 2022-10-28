package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private OrderTable orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order(final Long id, final OrderTable orderTable, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long id, final OrderTable orderTable, final String orderStatus,
                 final LocalDateTime orderedTime) {
        this(id, orderTable, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(final OrderTable orderTable, final String orderStatus, final LocalDateTime orderedTime) {
        this(null, orderTable, orderStatus, orderedTime, new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public boolean isCompleted() {
        return this.orderStatus.equals(OrderStatus.COMPLETION.name());
    }
}
