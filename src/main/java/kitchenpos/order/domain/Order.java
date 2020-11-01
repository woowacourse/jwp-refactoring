package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long tableId, String orderStatus) {
        this(null, tableId, orderStatus, LocalDateTime.now(), Collections.emptyList());
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

    public void changeId(Long id) {
        this.id = id;
    }

    public void changeOrderTableId(Long id) {
        this.orderTableId = id;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus.name();
    }

    public void changeOrderedTime(LocalDateTime localDateTime) {
        this.orderedTime = localDateTime;
    }

    public void changeOrderLineItems(List<OrderLineItem> savedOrderLineItems) {
        this.orderLineItems = savedOrderLineItems;
    }
}
