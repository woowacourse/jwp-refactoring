package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long orderTableId, String orderStatus,
                 List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public List<Long> getOrderMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public void validateOrderLineItemsSize(long validSize) {
        if (orderLineItems.size() != validSize) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(Order order) {
        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        this.orderStatus = orderStatus.name();
    }
}
