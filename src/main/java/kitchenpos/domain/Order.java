package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private final Long orderTableId;
    private OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new ArrayList<>();
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isCompletion() {
        return orderStatus == OrderStatus.COMPLETION;
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
