package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private final Long orderTableId;
    private String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this(orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void updateStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
