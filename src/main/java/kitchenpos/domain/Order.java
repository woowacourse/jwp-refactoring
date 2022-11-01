package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(final Long id,
                 final Long orderTableId,
                 final String orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems.addAll(orderLineItems);
    }

    public static Order ofNullId(final Long orderTableId,
                                 final String orderStatus,
                                 final LocalDateTime orderedTime,
                                 final List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public void addAllOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void updateOrderStatus(final String status) {
        if (OrderStatus.isAvailable(status)) {
            this.orderStatus = status;
        }
    }

    public boolean isCompletion() {
        return Objects.equals(OrderStatus.COMPLETION.name(), this.getOrderStatus());
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
        return orderLineItems;
    }
}
